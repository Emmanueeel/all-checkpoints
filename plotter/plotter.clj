;; plotter.clj
;; Функциональная реализация эмулятора плоттера
;; Переведено с TypeScript на Clojure
;; КТ_2

;; Константы и типы данных

(def carriage-state
  "Состояние каретки: :up - поднята, :down - опущена"
  {:up :up, :down :down})

(def line-color
  "Доступные цвета линии"
  {:black "черный", :red "красный", :green "зелёный"})

;; Создание начального состояния

(defn create-plotter-state
  "Создаёт начальное состояние плоттера
    Позиция: (0,0), Угол: 0°, Цвет: черный, Каретка: поднята"
  ([]
    {:position {:x 0.0 :y 0.0}
    :angle 0.0
    :color (:black line-color)
    :carriage-state (:up carriage-state)})
  ([position angle color carriage]
    {:position position
    :angle angle
    :color color
    :carriage-state carriage}))

;; Вспомогательные функции

(defn- draw-line
  "Чертит линию от координат from к координатам to"
  [printer from to color]
  (printer (format "...Чертим линию из (%s, %s) в (%s, %s) используя %s цвет."
                    (:x from) (:y from) (:x to) (:y to) color)))

(defn- calc-new-position
  "Вычисляет и возвращает новую позицию"
  [distance angle current]
  (let [angle-in-rads (* angle (/ Math/PI 180.0))
        x (+ (:x current) (* distance (Math/cos angle-in-rads)))
        y (+ (:y current) (* distance (Math/sin angle-in-rads)))]
    {:x (double (Math/round x)) :y (double (Math/round y))}))

;; Основные команды плоттера (5 команд)

(defn move
  "Команда 1: Перемещает каретку на расстояние distance.
    Если каретка опущена - чертит линию.
    Возвращает новый PlotterState"
  [printer distance state]
  (let [new-position (calc-new-position distance (:angle state) (:position state))]
    (if (= (:carriage-state state) (:down carriage-state))
      (draw-line printer (:position state) new-position (:color state))
      (printer (format "Передвигаем на %s от точки (%s, %s)"
                        distance (:x (:position state)) (:y (:position state)))))
    (assoc state :position new-position)))

(defn turn
  "Команда 2: Поворачивает каретку на угол angle.
    Возвращает новый PlotterState"
  [printer angle state]
  (printer (format "Поворачиваем на %s градусов" angle))
  (update state :angle #(mod (+ % angle) 360.0)))

(defn carriage-up
  "Команда 3: Поднимает каретку.
    Возвращает новый PlotterState"
  [printer state]
  (printer "Поднимаем каретку")
  (assoc state :carriage-state (:up carriage-state)))

(defn carriage-down
  "Команда 3: Опускает каретку.
    Возвращает новый PlotterState"
  [printer state]
  (printer "Опускаем каретку")
  (assoc state :carriage-state (:down carriage-state)))

(defn set-color
  "Команда 4: Устанавливает цвет печати в color.
    Возвращает новый PlotterState"
  [printer color state]
  (printer (format "Устанавливаем %s цвет линии." color))
  (assoc state :color color))

(defn set-position
  "Команда 5: Устанавливает позицию каретки в position.
    Возвращает новый PlotterState"
  [printer position state]
  (printer (format "Устанавливаем позицию каретки в (%s, %s)." (:x position) (:y position)))
  (assoc state :position position))

;; Функции для черчения фигур

(defn draw-triangle
  "Чертит треугольник со сторонами size"
  [printer size state]
  (->> state
        (carriage-down printer)
        (move printer size)
        (turn printer 120.0)
        (move printer size)
        (turn printer 120.0)
        (move printer size)
        (turn printer 120.0)
        (carriage-up printer)))

(defn draw-square
  "Чертит квадрат со сторонами size"
  [printer size state]
  (->> state
        (carriage-down printer)
        (move printer size)
        (turn printer 90.0)
        (move printer size)
        (turn printer 90.0)
        (move printer size)
        (turn printer 90.0)
        (move printer size)
        (turn printer 90.0)
        (carriage-up printer)))

;; Демонстрация (повторяет пример на TypeScript)

(defn -main
  "Главная функция - демонстрирует работу плоттера"
  [& _]
  (println "\n=== Чертим треугольник и квадрат ===\n")
  (let [printer println
        initial-state (create-plotter-state)]
    (->> initial-state
          (draw-triangle printer 100.0)
          (set-position printer {:x 10.0 :y 10.0})
          (set-color printer (:red line-color))
          (draw-square printer 80.0))
    (println "\n=== Готово! ===")))


(-main)