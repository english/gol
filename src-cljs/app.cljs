(ns gol.app
  (:require [gol.games :as games]))

(def cell-size 10)
(def canvas (.getElementById js/document "canvas"))
(def ctx (.getContext canvas "2d"))
(def game-state (atom games/gun))

(defn log [& items]
  (.log js/console (apply str items)))

(defn neighbours [[x y]]
  (for [dx [-1 0 1]
        dy (if (zero? dx) [-1 1] [-1 0 1])]
    [(+ dx x) (+ dy y)]))

(defn tick [cells]
  (set (for [[cell n] (frequencies (mapcat neighbours cells))
             :when (or (= n 3) (and (= n 2) (cells cell)))]
         cell)))

(defn render [live-cells]
  (.clearRect ctx 0 0 (.-width canvas) (.-height canvas))
  (doseq [[x y] live-cells]
    (.fillRect ctx (* x cell-size) (* y cell-size) cell-size cell-size)))

(defn game-loop [_]
  (render @game-state)
  (swap! game-state tick)
  (.requestAnimationFrame js/window game-loop))

(.requestAnimationFrame js/window game-loop)
