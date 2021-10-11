(ns cartagena.swingUI.drawing
  (:require [clojure.java.io :as io]
            [cartagena.data-abstractions.square-bis :refer [pieces-in pieces-to-vector]]
            [cartagena.data-abstractions.board :refer [square square-type]]
            [cartagena.data-abstractions.player :refer [color cards]]
            [cartagena.data-abstractions.deck :refer [cards-amounts]]
            [cartagena.swingUI.shaping :refer [track-shapes hand-shapes]])
  (:import (java.awt Color Graphics2D)
           (javax.imageio ImageIO)
           (java.awt.geom Ellipse2D$Double RectangularShape)))

(def color-map
  {:red Color/RED :green Color/GREEN :blue Color/BLUE :yellow Color/YELLOW :brown (Color. 139 69 19)})

(defn centered-circle [cx cy r]
  (Ellipse2D$Double. (- cx r) (- cy r) (* r 2) (* r 2)))

(defn load-image [s] (-> s
                         io/input-stream
                         ImageIO/read))

(def images
  {:start (load-image "./resources/cards/hands6.png")
   :boat   (load-image "./resources/cards/sail1.png")
   :keys   (load-image "./resources/cards/old45.png")
   :bottle (load-image "./resources/cards/wine47.png")
   :flag   (load-image "./resources/cards/halloween16.png")
   :hat    (load-image "./resources/cards/fedora.png")
   :pistol (load-image "./resources/cards/old3.png")
   :sword  (load-image "./resources/cards/sword1.png")})

(defn draw-image [graphics image shape]
  (.drawImage graphics image (.getMinX shape) (.getMinY shape) (.getMaxX shape) (.getMaxY shape)
              0 0 (.getWidth image) (.getHeight image) nil))

(defn draw-squares [^Graphics2D graphics board]
  (doseq [square-index (range (count board))]
    (let [image ((square-type board square-index) images)
          ^RectangularShape shape (get track-shapes square-index)]
      (doto graphics
        (.draw shape) ; draw the square
        (draw-image image shape))))) ; draw the image on top 

(defmulti draw-piece
  (fn [_ pieces _]
    (count (pieces-to-vector pieces))))

(defmethod draw-piece 0 [_ _ _])

(defmethod draw-piece 1 [^Graphics2D graphics pieces boundary]
  (let [piece (first (pieces-to-vector pieces))
        color (piece color-map)
        cx (.getCenterX boundary)
        cy (.getCenterY boundary)
        r (* (.getWidth boundary) 0.25)
        shape (centered-circle cx cy r)]
    (.setColor graphics color)
    (.fill graphics shape)))

(defmethod draw-piece 2 [^Graphics2D graphics pieces boundary]
  (let [piece1 (first (pieces-to-vector pieces))
        color1 (piece1 color-map)
        piece2 (second (pieces-to-vector pieces))
        color2 (piece2 color-map)
        cx (.getCenterX boundary)
        cy (.getCenterY boundary)
        r (/ (.getWidth boundary) 3.0)]
    (.setColor graphics color1)
    (.fill graphics (Ellipse2D$Double. (- cx (* r 0.5)) (+ (- cy (* r 0.5)) (/ r 2.0)) r r))
    (.setColor graphics color2)
    (.fill graphics (Ellipse2D$Double. (- cx (* r 0.5)) (- (- cy (* r 0.5)) (/ r 2.0)) r r))))

(defmethod draw-piece :default [^Graphics2D graphics pieces boundary]
  (let [p (pieces-to-vector pieces)
        n (count p)
        cx (.getCenterX boundary)
        cy (.getCenterY boundary)
        r (* 3.0 (Math/sqrt (.getWidth boundary)))
        theta (/ Math/PI (inc n) 0.5)
        cl (* 2.0 r (Math/sin (* theta 0.5)))]
    (doseq [i (range n)]
      (let [piece (p i)
            color (piece color-map)
            y (* r (Math/sin (* i theta)))
            x (* r (Math/cos (* i theta)))
            shape (Ellipse2D$Double. (- cx x (* cl 0.5)) (- cy y (* cl 0.5)) cl cl)]
        (.setColor graphics color)
        (.fill graphics shape)))))

(defn draw-pieces
  ([^Graphics2D graphics pieces boundary]
   (draw-piece graphics pieces boundary))
  ([^Graphics2D graphics board]
   (doseq [square-index (range (count board))]
     (let [local-pieces (pieces-in (square board square-index))
           shape (get track-shapes square-index)]
       (draw-pieces graphics local-pieces shape)))))

(defn draw-cards [^Graphics2D graphics player]
  (let [color (color-map (color player))
        hand (cards player)]
    (doseq [hand-shape hand-shapes]
      (let [shape (key hand-shape)
            card-type (val hand-shape)
            image (card-type images)
            dx1 (.getMinX shape)
            dy1 (.getMinY shape)]
        (doto graphics
          (.setPaint color)
          (.draw shape)
          (draw-image image shape)
          (.setPaint Color/BLACK)
          ; (println "Card type:" (str (card-type (cards-amounts hand))))
          (.drawString (str (card-type (cards-amounts hand))) (float (+ dx1 (.getWidth shape))) (float (+ dy1 (.getHeight shape)))))))))
