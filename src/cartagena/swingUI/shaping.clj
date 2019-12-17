(ns cartagena.swingUI.shaping
  (:require [cartagena.core :refer [card-types]])
  (:import (java.awt.geom Rectangle2D$Double)))


(def cards-size 50)

(def grid [[0 0] [1 0] [2 0] [3 0] [4 0] [5 0] [6 0] [7 0]
                                                     [7 1]
                                                     [7 2]
           [7 3] [6 3] [5 3] [4 3] [3 3] [2 3] [1 3] [0 3]
           [0 4]
           [0 5]
           [0 6] [1 6] [2 6] [3 6] [4 6] [5 6] [6 6] [7 6]
                                                     [7 7]
                                                     [7 8]
           [7 9] [6 9] [5 9] [4 9] [3 9] [2 9] [1 9] [0 9]])

(defn create-square [coord dim]
  (Rectangle2D$Double. (* dim (first coord)) (* dim (second coord)) dim dim))

(def track-shapes (mapv #(create-square % cards-size) grid))

(def hand-shapes
  (zipmap
    (map #(create-square [9 %] cards-size) (range (count card-types)))
    card-types))

