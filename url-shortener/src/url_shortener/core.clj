(ns url-shortener.core
  (:require [url-shortener.server :as server]
            [url-shortener.cli :as cli])
  (:gen-class))

(defn -main [& args]
  (if (seq args)
    ;; Если переданы аргументы — запускаем сервер
    (let [port (first args)]
      (server/start-server port))
    ;; Если аргументов нет — запускаем клиент
    (cli/run-client)))