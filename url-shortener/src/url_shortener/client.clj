(ns url-shortener.client
    (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def ^:dynamic *base-url* "http://localhost:8080")

;; POST /normal-url  →  вернуть short-url или nil при ошибке
(defn create-url [normal-url]
    (try
    (let [response (http/post (str *base-url* "/normal-url")
                                    {:content-type :json
                                    :body (json/generate-string {:url normal-url})
                                    :throw-exceptions false})]
        (if (= 200 (:status response))
        (:short-url (json/parse-string (:body response) true))
        nil))
    (catch Exception _ nil)))

;; GET /:short-url  →  вернуть normal-url или nil
(defn get-url [short-url]
    (try
    (let [response (http/get (str *base-url* "/" short-url)
                                {:throw-exceptions false})]
        (if (= 200 (:status response))
        (:url (json/parse-string (:body response) true))
        nil))
    (catch Exception _ nil)))

;; PUT /:short-url  с телом {"url": "новый URL"}  →  true/false
(defn update-url [short-url new-normal-url] 
    (try
    (let [response (http/put (str *base-url* "/" short-url)
                                {:content-type :json
                                :body (json/generate-string {:url new-normal-url})
                                :throw-exceptions false})]
        (= 200 (:status response)))
    (catch Exception _ false)))

;; DELETE /:short-url  →  true/false
(defn delete-url [short-url]
    (try
    (let [response (http/delete (str *base-url* "/" short-url)
                                {:throw-exceptions false})]
        (= 200 (:status response)))
    (catch Exception _ false)))