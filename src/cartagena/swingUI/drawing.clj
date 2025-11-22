(ns cartagena.swingUI.drawing
  (:require [clojure.java.io :as io]
            [cartagena.data-abstractions.square-bis :as sq]
            [cartagena.data-abstractions.board :as b]
            [cartagena.data-abstractions.player-bis :as p]
            [cartagena.data-abstractions.deck :as d]
            [cartagena.swingUI.shaping :refer [track-shapes hand-shapes]])
  (:import (java.awt Color Graphics2D)
           (javax.imageio ImageIO)
           (java.awt.geom Ellipse2D$Double RectangularShape)))

;; Swing rendering helpers for Cartagena.
;;
;; This namespace is responsible for drawing the game state:
;;   - board squares and their background images
;;   - pieces on the board
;;   - players' hands and card counts
;;
;; It consumes pure data structures from the data-abstractions layer and the
;; geometric shapes from cartagena.swingUI.shaping, and issues side-effecting
;; drawing calls on a Graphics2D context.

(def color-map
  "Map from player color keywords to Java AWT Color instances."
  {:red Color/RED :green Color/GREEN :blue Color/BLUE :yellow Color/YELLOW :brown (Color. 139 69 19)})

(defn centered-circle
  "Create a circle shape of radius r centered at (cx, cy)."
  [cx cy r]
  (Ellipse2D$Double. (- cx r) (- cy r) (* r 2) (* r 2)))

(defn load-image
  "Load an image from the given path or classpath resource."
  [s]
  (-> s
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

(defn draw-image
  "Draw the given image scaled to fill the provided shape bounds."
  [graphics image shape]
  (.drawImage graphics image (.getMinX shape) (.getMinY shape) (.getMaxX shape) (.getMaxY shape)
              0 0 (.getWidth image) (.getHeight image) nil))

(defn- draw-square
  "Draw a single board square image at the given index."
  [graphics board index]
  (let [image ((b/square-type board index) images)
        ^RectangularShape shape (get track-shapes index)]
    (doto graphics
      (.draw shape) ; draw the square
      (draw-image image shape)))) ; draw the image on top 

(defn draw-board
  "Draw the full board (all squares) on the given Graphics2D."
  [^Graphics2D graphics board]
  (doseq [square-index (range (count board))]
    (draw-square graphics board square-index)))

(defmulti draw-the-pieces
  (fn [_ pieces _]
    (count (sq/pieces-to-vector pieces))))

(defmethod draw-the-pieces 0 [_ _ _])

(defmethod draw-the-pieces 1 [^Graphics2D graphics pieces boundary]
  (let [piece (first (sq/pieces-to-vector pieces))
        color (piece color-map)
        cx (.getCenterX boundary)
        cy (.getCenterY boundary)
        r (* (.getWidth boundary) 0.25)
        shape (centered-circle cx cy r)]
    (.setColor graphics color)
    (.fill graphics shape)))

(defmethod draw-the-pieces 2 [^Graphics2D graphics pieces boundary]
  (let [piece1 (first (sq/pieces-to-vector pieces))
        color1 (piece1 color-map)
        piece2 (second (sq/pieces-to-vector pieces))
        color2 (piece2 color-map)
        cx (.getCenterX boundary)
        cy (.getCenterY boundary)
        r (/ (.getWidth boundary) 3.0)]
    (.setColor graphics color1)
    (.fill graphics (Ellipse2D$Double. (- cx (* r 0.5)) (+ (- cy (* r 0.5)) (/ r 2.0)) r r))
    (.setColor graphics color2)
    (.fill graphics (Ellipse2D$Double. (- cx (* r 0.5)) (- (- cy (* r 0.5)) (/ r 2.0)) r r))))

(defmethod draw-the-pieces :default [^Graphics2D graphics pieces boundary]
  (let [pp (sq/pieces-to-vector pieces)
        n (count pp)
        cx (.getCenterX boundary)
        cy (.getCenterY boundary)
        r (* 3.0 (Math/sqrt (.getWidth boundary)))
        theta (/ Math/PI (inc n) 0.5)
        cl (* 2.0 r (Math/sin (* theta 0.5)))]
    (doseq [i (range n)]
      (let [piece (pp i)
            color (piece color-map)
            y (* r (Math/sin (* i theta)))
            x (* r (Math/cos (* i theta)))
            shape (Ellipse2D$Double. (- cx x (* cl 0.5)) (- cy y (* cl 0.5)) cl cl)]
        (.setColor graphics color)
        (.fill graphics shape)))))

(defn draw-pieces
  "Draw pieces either for a single square (arity 3) or for the whole board (arity 2)."
  ([^Graphics2D graphics pieces boundary]
   (draw-the-pieces graphics pieces boundary))
  ([^Graphics2D graphics board]
   (doseq [square-index (range (count board))]
     (let [local-pieces (b/pieces-in board square-index)
           shape (get track-shapes square-index)]
       (draw-pieces graphics local-pieces shape)))))

(defn draw-cards
  "Draw the active player's hand and card counts."
  [^Graphics2D graphics player]
  (let [color (color-map (p/color player))
        hand (p/cards player)]
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
          (.drawString (str (card-type (d/cards-amounts hand)))
                       (float (+ dx1
                                 (.getWidth shape)))
                       (float (+ dy1
                                 (.getHeight shape)))))))))

