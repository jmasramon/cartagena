(ns cartagena.swingUI.swingUI
  (:require [cartagena.core :refer [pirate-colors]]
            [cartagena.data-abstractions.moves :as mvs]
            [cartagena.data-abstractions.game :as g]
            [cartagena.swingUI.shaping :refer [track-shapes]]
            [cartagena.swingUI.drawing :as dw :refer [draw-cards draw-pieces]])

  (:import (java.awt BorderLayout Component Shape)
           (javax.swing JFrame JPopupMenu JMenuItem JOptionPane JLabel)
           (java.awt.event MouseAdapter ActionListener MouseEvent)))


(defn create-action [parent action]
  (proxy [ActionListener] []
    (actionPerformed [_]
      (action)
      (.repaint parent))))

(defn popup
  "Main game logic is called from here"
  [parent ^MouseEvent event game square-index]
  (let [playable-cards (g/playable-cards @game)
        f (fn [menu]
            (doseq [cardMenuItem (zipmap playable-cards (map #(JMenuItem. (str "Play " %)) playable-cards))]
              (.addActionListener (val cardMenuItem) (create-action parent #(swap! game mvs/play-card (first (key cardMenuItem)) square-index)))
              (.add menu (val cardMenuItem))))]
    (if (.isPopupTrigger event)
      (.show (doto (JPopupMenu.)
               f
               (.add (doto (JMenuItem. "Fall Back") (.addActionListener (create-action parent #(swap! game mvs/fall-back square-index)))))
               (.add (doto (JMenuItem. "Pass") (.addActionListener (create-action parent #(swap! game mvs/pass))))))
             (.getComponent event) (.x (.getPoint event)) (.y (.getPoint event)))
      nil)))

(defn get-click-index [x y]
  (first (filter #(.contains ^Shape (get track-shapes %) x y) (range (count track-shapes)))))

(defn clicker [parent game]
  (let [click-handler #(when-let [clicked (get-click-index (-> % .getPoint .x) (-> % .getPoint .y))]
                         (popup parent %
                                game clicked))]
    (proxy [MouseAdapter] []
      (mousePressed [event] (click-handler event))
      (mouseReleased [event] (click-handler event)))))

(defn draw-board [game]
  (let [component (proxy [Component] []
                    (paint [graphics]
                      (doto graphics
                        (dw/draw-board (g/board @game)) 
                        (draw-cards (g/active-player @game))
                        (draw-pieces (g/board @game)))))]
    (.addMouseListener component (clicker component game))
    component))

(defn frame [initial-game-state exit-condition]
  (let [frame (JFrame. "Cartagena!")
        game (atom initial-game-state)]
    (doto frame
      (.setSize 600 600)
      (.add ^Component (draw-board game) BorderLayout/CENTER)
      (.add (JLabel. "Right-click on square with piece to make a move.") BorderLayout/SOUTH)
      (.setDefaultCloseOperation exit-condition)
      (.setVisible true)
      (.repaint))
    frame))

(defn -main []
  (let [n (JOptionPane/showInputDialog nil "Enter players (2-5):")
        colors (take (read-string n) pirate-colors)
        players (map #(str % "-player") colors)]
    (println "Num players:" n "Colors:" colors "Players names:" players)
    (frame
     (g/make-game (count colors)) ; filter just the chosen players
     JFrame/EXIT_ON_CLOSE)))
