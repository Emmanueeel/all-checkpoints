(ns url-shortener.cli
    (:require [url-shortener.client :as client]
            [clojure.string :as str]))

(defn- read-line-trim []
    (str/trim (or (read-line) "")))

(defn- show-menu []
    (println)
    (println "=== URL Shortener ===")
    (println "1. Создать")
    (println "2. Показать")
    (println "3. Изменить")
    (println "4. Удалить")
    (println "5. Выйти"))

(defn- do-create []
    (println "Введите обычный URL для сокращения:")
    (print "  > ") (flush)
    (let [normal-url (read-line-trim)]
        (if (empty? normal-url)
            (println "Ответ: Ошибка")
            (do
        (println "Отправка запроса...")
        (let [result (client/create-url normal-url)]
            (println (str "Ответ: " (or result "Ошибка"))))))))

(defn- do-get []
    (println "Введите короткий URL:")
    (print "  > ") (flush)
    (let [short-url (read-line-trim)]
        (if (empty? short-url)
            (println "Ответ: Ошибка")
            (do
        (println "Отправка запроса...")
        (let [result (client/get-url short-url)]
            (println (str "Ответ: " (or result "Ошибка"))))))))

(defn- do-update []
    (println "Введите через пробел короткий URL и новый обычный URL:")
    (print "  > ") (flush)
    (let [line (read-line-trim)
        parts (str/split line #"\s+" 2)]
    (if (not= 2 (count parts))
        (println "Ответ: Ошибка")
        (let [[short-url normal-url] parts]
            (println "Отправка запроса...")
            (if (client/update-url short-url normal-url)
            (println "Ответ: OK")
            (println "Ответ: Ошибка"))))))

(defn- do-delete []
    (println "Введите короткий URL:")
    (print "  > ") (flush)
    (let [short-url (read-line-trim)]
    (if (empty? short-url)
        (println "Ответ: Ошибка")
        (do
            (println "Отправка запроса...")
            (if (client/delete-url short-url)
                (println "Ответ: OK")
                (println "Ответ: Ошибка"))))))

(defn run-client []
    (println "Клиент URL Shortener")
    (loop []
        (show-menu)
        (print "Выберите действие: ") (flush)
        (let [choice (read-line-trim)]
        (case choice
            "1" (do (do-create) (recur))
            "2" (do (do-get)    (recur))
            "3" (do (do-update) (recur))
            "4" (do (do-delete) (recur))
            "5" (println "Выход.")
            (do (println "Неверный выбор, попробуйте снова.") (recur))))))