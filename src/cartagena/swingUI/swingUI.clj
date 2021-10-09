(ns cartagena.swingUI.swingUI
  (:require [cartagena.core :refer [pirate-colors]]
            ;; improve data abstractions: only game and moves should be needed
            [cartagena.data-abstractions.game :refer [make-game players active-player]]
            [cartagena.data-abstractions.player :refer [cards color]]
            [cartagena.data-abstractions.deck :refer [playable-cards]]
            [cartagena.data-abstractions.moves :refer [pass play-card fall-back]]

            [cartagena.swingUI.shaping :refer [track-shapes]]
            [cartagena.swingUI.drawing :refer [draw-squares draw-cards draw-pieces]])

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
  [parent ^MouseEvent event cards game square-index]
  (let [playable-cards (playable-cards cards)
        f (fn [menu]
            (doseq [cardMenuItem (zipmap playable-cards (map #(JMenuItem. (str "Play " %)) playable-cards))]
              (.addActionListener (val cardMenuItem) (create-action parent #(swap! game play-card (first (key cardMenuItem)) square-index)))
              (.add menu (val cardMenuItem))))]
    (if (.isPopupTrigger event)
      (.show (doto (JPopupMenu.)
               f
               (.add (doto (JMenuItem. "Fall Back") (.addActionListener (create-action parent #(swap! game fall-back square-index)))))
               (.add (doto (JMenuItem. "Pass") (.addActionListener (create-action parent #(swap! game pass))))))
             (.getComponent event) (.x (.getPoint event)) (.y (.getPoint event))))))

(defn get-click-index [x y]
  (first (filter #(.contains ^Shape (get track-shapes %) x y) (range (count track-shapes)))))

(defn clicker [parent game]
  (let [click-handler #(when-let [clicked (get-click-index (-> % .getPoint .x) (-> % .getPoint .y))]
                         (let [color (color (active-player @game))]
                           (popup parent %
                                  (cards (players @game) color)
                                  game clicked)))]
    (proxy [MouseAdapter] []
      (mousePressed [event] (click-handler event))
      (mouseReleased [event] (click-handler event)))))

(defn draw-board [game]
  (let [component (proxy [Component] []
                    (paint [graphics]
                      (doto graphics
                        (draw-squares (:board @game))
                        (draw-cards (active-player @game))
                        (draw-pieces (:board @game)))))]
    (.addMouseListener component (clicker component game))
    component))

(defn frame [initial-game-state exit-condition]
  (let [frame (JFrame. "Cartagena!")
        ; first-player (first (keys (:turn-order initial-game-state)))
        ; started (cartagena.core/start-turn initial-game-state first-player)
        ;game (atom (almost-win started))
        ; game (atom started)
        game (atom initial-game-state)]
    ; (setup-winner-watch game)
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
     (make-game (count colors)) ; filter just the chosen players
     JFrame/EXIT_ON_CLOSE)))
