(ns gol.core-test
  (:require [clojure.test :refer :all]
            [gol.core :refer :all]))

(deftest game-of-life
  (testing "Any live cell with fewer than two live neighbours dies"
    (is (not ((tick #{[0 0]})
              [0 0])))
    (is (not ((tick #{[0 0]
                      [0 1] [1 1]             [4 1]})
              [4 1]))))

  (testing "Any live cell with two or three live neighbours lives on to the next generation."
    (let [live-cells (tick #{[0 0] [1 0] [2 0]       [4 0]
                             [0 1] [1 1]                  })]
      (is (live-cells [0 0]))
      (is (live-cells [0 1]))
      (is (live-cells [2 0]))
      (is (not (live-cells [4 0])))))

  (testing "Any live cell with more than three live neighbours dies, as if by overcrowding."
    (is (not ((tick #{[0 0] [1 0] [2 0]
                      [0 1] [1 1] [2 1]
                      [0 2] [1 2] [2 2]})
              [1 1]))))

  (testing "Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction."
    (let [live-cells (tick #{[0 0] [1 0] [2 0]
                             [0 1] [1 1] [2 1]
                             [0 2] [1 2]      })]
      (is (live-cells [2 2])))

    (let [size-x 4
          size-y 4
          live-cells (tick #{[0 0] [1 0] [2 0];[3 0]
                             [0 1] [1 1] [2 1];[3 1]
                             [0 2] [1 2] [2 2];[3 2]
                            ;[0 3] [1 3] [2 3] [3 3]
                             })]
      (is (live-cells [1 3]))
      (is (live-cells [3 1]))))

  (testing "The whole shebang"
    (let [live-cells (tick #{[0 0]       [2 0]
                             [0 1] [1 1] [2 1]
                                   [1 2]       [3 2]})]
      (is (= live-cells
             #{[0 0]       [2 0]
               [0 1]             [3 1]
               [0 2] [1 2]            })))))
