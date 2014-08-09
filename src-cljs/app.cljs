(ns gol.app
  (:require [gol.core :as gol]))

;; Games
(def glider #{[2 1] [3 2] [1 3] [2 3] [3 3]})
(def blinker #{[2 0] [2 1] [2 2]})
(def toad #{[2 2] [3 2] [4 2] [1 3] [2 3] [3 3]})
(def diehard #{[7 1] [1 2] [2 2] [2 3] [6 3] [7 3] [8 3]})
(def gun #{[24 0] [22 1] [24 1] [12 2] [13 2] [20 2] [21 2] [34 2] [35 2] [11 3] [15 3] [20 3] [21 3] [34 3] [35 3] [0 4] [1 4] [10 4] [16 4] [20 4] [21 4] [0 5] [1 5] [10 5] [14 5] [16 5] [17 5] [22 5] [24 5] [10 6] [16 6] [24 6] [11 7] [15 7] [12 8] [13 8]})

(def cell-size 10)
(def canvas (.getElementById js/document "canvas"))
(def ctx (.getContext canvas "2d"))
(def game-state (atom gun))

(defn log [& items]
  (.log js/console (apply str items)))

(defn render [live-cells]
  (.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
  (doseq [[x y] live-cells]
    (.fillRect ctx (* x cell-size) (* y cell-size) cell-size cell-size)))

(defn game-loop [_]
  (render @game-state)
  (swap! game-state gol/tick)
  (.requestAnimationFrame js/window game-loop))

(.requestAnimationFrame js/window game-loop)
