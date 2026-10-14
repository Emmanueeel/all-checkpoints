(ns user)
;; Игра "Угадай число" наоборот
;; КТ_1

;; Границы текущего диапазона поиска
(def low (atom nil))
(def high (atom nil))

;; Последнее предположение программы
(def current-guess (atom nil))

;; start: начинает новую игру в диапазоне [n, m]
(defn start [n m]
  (reset! low n)
  (reset! high m)
  (reset! current-guess nil)
  "Я готов...")

;; guess-my-number: возвращает середину текущего диапазона
(defn guess-my-number []
  (when (or (nil? @low) (nil? @high))
    (throw (Exception. "Сначала вызовите start с указанием диапазона")))
  (let [guess (int (Math/ceil (/ (+ @low @high) 2.0)))]
    (reset! current-guess guess)
    guess))

;; smaller: загаданное число меньше текущего предположения
(defn smaller []
  (when (nil? @current-guess)
    (throw (Exception. "Сначала вызовите guess-my-number")))
  (reset! high (dec @current-guess))
  (guess-my-number))

;; bigger: загаданное число больше текущего предположения
(defn bigger []
  (when (nil? @current-guess)
    (throw (Exception. "Сначала вызовите guess-my-number")))
  (reset! low (inc @current-guess))
  (guess-my-number))





