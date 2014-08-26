(ns gol.core
  (:require [clojure.string :as s]))

;; Game logic
(defn neighbours [[x y]]
  (for [dx [-1 0 1]
        dy (if (zero? dx) [-1 1] [-1 0 1])]
    [(+ dx x) (+ dy y)]))

(defn tick [cells]
  (set (for [[cell n] (frequencies (mapcat neighbours cells))
             :when (or (= n 3) (and (= n 2) (cells cell)))]
         cell)))

;; Games
(def games {:glider  #{[2 1] [3 2] [1 3] [2 3] [3 3]}
            :blinker #{[2 0] [2 1] [2 2]}
            :toad    #{[2 2] [3 2] [4 2] [1 3] [2 3] [3 3]}
            :diehard #{[7 1] [1 2] [2 2] [2 3] [6 3] [7 3] [8 3]}
            :gun     #{[24 0] [22 1] [24 1] [12 2] [13 2] [20 2] [21 2] [34 2] [35 2] [11 3] [15 3] [20 3] [21 3] [34 3] [35 3] [0 4] [1 4] [10 4] [16 4] [20 4] [21 4] [0 5] [1 5] [10 5] [14 5] [16 5] [17 5] [22 5] [24 5] [10 6] [16 6] [24 6] [11 7] [15 7] [12 8] [13 8]}})

(defonce game-state (atom nil))

(def cell-size 10)

(defn log [& items]
  (.log js/console (apply str items)))

;; Make NodeList seq-able
(extend-type js/NodeList
  ISeqable
  (-seq [array] (array-seq array 0)))

(defn render [live-cells]
  (let [canvas (.getElementById js/document "canvas")
        ctx    (.getContext canvas "2d")]
    (.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
    (doseq [[x y] live-cells]
      (.fillRect ctx (* x cell-size) (* y cell-size) cell-size cell-size))))

(defn clear-active-classes! []
  ((comp dorun map) #(set! (.-className %) "")
   (.querySelectorAll js/document "nav li")))

(defn set-active-class! [game]
  (set! (.-className (find-nav-item game)) "active"))

(defn find-nav-item [game]
  (let [nav-items (.querySelectorAll js/document "nav li")]
    (first (filter (fn [nav-item]
                     (= (.toLowerCase (.-textContent nav-item))
                        (.toLowerCase (name game))))
                   nav-items))))

(defn handle-nav-click [game]
  (fn [e]
    (clear-active-classes!)
    (set-active-class! game)
    (swap! game-state #(get games game))))

(defn create-nav-item [game]
  (let [li (.createElement js/document "li")
        a  (.createElement js/document "a")]
    (set! (.-onclick a) (handle-nav-click game))
    (set! (.-text a) (s/capitalize (name game)))
    (set! (.-href a) (str "#" (name game)))
    (.appendChild li a)
    li))

(defn render-game-links []
  (let [nav (.getElementById js/document "nav")
        ul (.createElement js/document "ul")]
    (.setAttribute ul "id" "nav")
    (.appendChild nav ul)
    (doseq [[game-name game] games]
      (.appendChild ul (create-nav-item game-name)))
    (.click (.querySelector js/document "nav a"))))

(defn game-loop [_]
  (render @game-state)
  (swap! game-state tick)
  (js/setTimeout (fn []
    (.requestAnimationFrame js/window game-loop)) 300))

(defn ^:export init []
  (render-game-links)
  (.requestAnimationFrame js/window game-loop))
