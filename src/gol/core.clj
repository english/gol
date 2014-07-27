(ns gol.core)

(defn neighbours
  "Given a cell (x and y tuple) and a set of cells, returns a seq of the cell's
  neighbouring cells"
  [[x y] cells]
  (filter #(or (= [(inc x) y]       %)
               (= [(inc x) (inc y)] %)
               (= [(inc x) (dec y)] %)
               (= [(dec x) y]       %)
               (= [(dec x) (inc y)] %)
               (= [(dec x) (dec y)] %)
               (= [x       (inc y)] %)
               (= [x       (dec y)] %))
          cells))

(defn full-game
  "Seq of cells of size x by y"
  [size-x size-y]
  (for [x (range (inc size-x))
        y (range (inc size-y))]
    [x y]))

(defn dead-cells
  "Returns corresponding dead cells for given a set of live-cells"
  [live-cells]
  (let [max-x (apply max (map first live-cells))
        max-y (apply max (map second live-cells))]
    (filter #(not (live-cells %))
            (full-game max-x max-y))))

(defn should-die?
  [cell live-cells]
  (let [num-neighbours (count (neighbours cell live-cells))]
    (and (< 1 num-neighbours)
         (> 4 num-neighbours))))

(defn should-live? [dead-cell live-cells]
  (let [live-neighbours (neighbours dead-cell live-cells)]
    (= 3 (count live-neighbours))))

(defn tick
  "Given a set of live-cells (x and y tuples), returns a set of surviving cells"
  [live-cells]
  (let [survivors (filter #(should-die? % live-cells)
                          live-cells)
        spawners  (filter #(should-live? % live-cells)
                          (dead-cells live-cells))]
    (set (concat survivors spawners))))
