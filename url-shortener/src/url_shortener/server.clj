(ns url-shortener.server
    (:require [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :as resp]
            [url-shortener.db :as db]))

;; POST /normal-url
;; Тело: {"url": "https://..."}  →  {"short-url": "abc12345"}
(defn handle-create [request]
    (let [normal-url (get-in request [:body :url])]
    (if (and normal-url (not (empty? normal-url)))
        (let [short-url (db/create-url normal-url)]
        (-> (resp/response {:short-url short-url})
            (resp/status 200)))
        (-> (resp/response {:error "URL is required"})
            (resp/status 400)))))

;; GET /:short-url  →  {"url": "https://..."}  или 404
(defn handle-get [short-url]
    (let [normal-url (db/get-url short-url)]
    (if normal-url
        (-> (resp/response {:url normal-url})
            (resp/status 200))
        (-> (resp/response {:error "Not found"})
            (resp/status 404)))))

;; PUT /:short-url  с телом {"url": "новый url"}  →  200 или 404
(defn handle-update [short-url new-normal-url]
    (if (db/update-url short-url new-normal-url)
    (-> (resp/response {:status "OK"})
        (resp/status 200))
    (-> (resp/response {:error "Not found"})
        (resp/status 404))))

;; DELETE /:short-url  →  200 или 404
(defn handle-delete [short-url] 
    (if (db/delete-url short-url)
    (-> (resp/response {:status "OK"})
        (resp/status 200))
    (-> (resp/response {:error "Not found"})
        (resp/status 404))))

(defroutes app-routes
    (POST "/normal-url" request (handle-create request))
    (GET "/:short-url" [short-url] (handle-get short-url))
    (PUT "/:short-url" request
    (handle-update (:short-url (:params request))
                        (get-in request [:body :url])))
    (DELETE "/:short-url" [short-url] (handle-delete short-url))
    (route/not-found {:error "Not found"}))

(def app
    (-> app-routes
        (wrap-json-body {:keywords? true})
        (wrap-json-response)
        (wrap-params)))

(defn start-server [port]
    (db/init-db)
    (println (str "Сервер запущен на порту " port))
    (run-jetty app {:port (Integer/parseInt (str port))
                    :join? true}))