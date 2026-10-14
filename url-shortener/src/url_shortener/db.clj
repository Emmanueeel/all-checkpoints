(ns url-shortener.db
    (:require [korma.db :as db]
            [korma.core :as k])
    (:import [java.util UUID]))

;; Подключение к SQLite. 
(def db-spec
    {:classname   "org.sqlite.JDBC"
        :subprotocol "sqlite"
        :subname     "url_shortener.db"})

(db/defdb sqlite db-spec)

;; Описание таблицы urls
(k/defentity urls)

;; Создание таблицы
(defn init-db []
    (k/exec-raw
        ["CREATE TABLE IF NOT EXISTS urls (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            short_url TEXT NOT NULL UNIQUE,
            normal_url TEXT NOT NULL
        )"]))

;; Генерация короткого URL
(defn generate-short-url []
    (subs (str (UUID/randomUUID)) 0 8))

;; Создать запись: сохранить normal-url, вернуть сгенерированный short-url
(defn create-url [normal-url]
        (let [short-url (generate-short-url)]
            (k/insert urls
                (k/values {:short_url  short-url
                                :normal_url normal-url}))
    short-url))

;; Получить normal-url по short-url. Если нет — вернуть nil
(defn get-url [short-url]
        (let [row (first (k/select urls
                                (k/where {:short_url short-url})))]
    (:normal_url row)))

;; Обновить normal-url по short-url. Вернуть true/false
(defn update-url [short-url new-normal-url]
    (let [affected (k/update urls
                            (k/set-fields {:normal_url new-normal-url})
                            (k/where {:short_url short-url}))]
    (pos? (or affected 0))))

;; Удалить запись по short-url. Вернуть true/false
(defn delete-url [short-url]
    (let [affected (k/delete urls
                            (k/where {:short_url short-url}))]
    (pos? (or affected 0))))