(ns cartagena.swingUI.swingUI
  (:require [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [cartagena.core :refer [card-types pirate-colors]]
            [cartagena.generators :refer [initial-state]]
            [cartagena.getters.mainEntities :refer [players]]
            [cartagena.getters.sdariEntities :refer [cards]]
            [cartagena.getters.semanticEntities :refer [active-player]]
            [cartagena.getters.otherEntities :refer [player-color player-cards cards-amounts playable-cards]]
            [cartagena.gameplay :refer [play-card fall-back pass]]
            [cartagena.swingUI.shaping :refer [track-shapes hand-shapes]]
            [cartagena.swingUI.drawing :refer [draw-squares draw-cards draw-pieces]]
            )

  (:import (java.awt Color BorderLayout Component Graphics2D Shape)
           (javax.imageio ImageIO)
           (javax.swing JFrame JPopupMenu JMenuItem JOptionPane JLabel SwingUtilities)
           (java.awt.geom Rectangle2D$Double Ellipse2D$Double RectangularShape)
           (java.awt.event MouseAdapter ActionListener MouseEvent)))


(defn create-action [parent action]
  (proxy [ActionListener] []
    (actionPerformed [_]
      (action)
      (.repaint parent))))

(defn popup [parent ^MouseEvent event cards game-state square-index]
  "Main game logic is called from here"
  (let [playable-cards (playable-cards cards)
        f (fn [menu]
            (doseq [cardMenuItem (zipmap playable-cards (map #(JMenuItem. (str "Play " %)) playable-cards))]
             ;  (println "playable-cards:" playable-cards)
            	; (println "popup playcard action cardMenuItem:" cardMenuItem "key:" (key cardMenuItem) "card:" (first (key cardMenuItem)) "square index:" square-index)
              (.addActionListener (val cardMenuItem) (create-action parent #(swap! game-state play-card (first (key cardMenuItem)) square-index)))
              (.add menu (val cardMenuItem))))]
    ; (println "cards" cards "(cards-amounts cards)" (cards-amounts cards) "Playable cards:" playable-cards)
    (if (.isPopupTrigger event)
      (.show (doto (JPopupMenu.)
               	f
              	(.add (doto (JMenuItem. "Fall Back") (.addActionListener (create-action parent #(swap! game-state fall-back square-index)))))
               	(.add (doto (JMenuItem. "Pass") (.addActionListener (create-action parent #(swap! game-state pass))))))
             	(.getComponent event) (.x (.getPoint event)) (.y (.getPoint event))))))

(defn get-click-index [x y]
  (first (filter #(.contains ^Shape (get track-shapes %) x y) (range (count track-shapes)))))

(defn clicker [parent game-state]
  (let [click-handler #(when-let [clicked (get-click-index (-> % .getPoint .x) (-> % .getPoint .y))]
                        	(let [color (player-color (active-player @game-state))]
                          		(popup parent %
                          		; (get-in @game-state [:players color :cards])
                          		(cards (players @game-state) (player-color (active-player @game-state)))
                          	 	game-state clicked)))]
    (proxy [MouseAdapter] []
      (mousePressed [event] (click-handler event))
      (mouseReleased [event] (click-handler event)))))

(defn draw-board [game-state]
  (let [component (proxy [Component] []
                    (paint [graphics]
                      (doto graphics
                      	; (println "Drawing board. Board:" (:board @game-state))
                        (draw-squares (:board @game-state))
                        ; (println "active Player:" (active-player @game-state))
                        (draw-cards (active-player @game-state))
                        (draw-pieces (:board @game-state))
                        )))]
    (.addMouseListener component (clicker component game-state))
    component))

(defn frame [initial-game-state exit-condition]
  (let [
  		  frame (JFrame. "Cartagena!")
        ; first-player (first (keys (:turn-order initial-game-state)))
        ; started (cartagena.core/start-turn initial-game-state first-player)
        ;game-state (atom (almost-win started))
        ; game-state (atom started)
        game-state (atom initial-game-state)
        ]
    ; (setup-winner-watch game-state)
    (doto frame
      (.setSize 600 600)
      (.add ^Component (draw-board game-state) BorderLayout/CENTER)
      (.add (JLabel. "Right-click on square with piece to make a move.") BorderLayout/SOUTH)
      (.setDefaultCloseOperation exit-condition)
      (.setVisible true)
      (.repaint))
    frame))

(defn -main []
  (let [n (JOptionPane/showInputDialog nil "Enter players (2-5):")
        colors (take (read-string n) pirate-colors)
        players (map #(partial {:color % :name (str % )}) colors)]
    (println "Num players:" n "Colors:" colors "Players names:" players)
    (frame
      ; (cartagena.core/init-game-state players)
      (initial-state (count colors)) ; filter just the chosen players
      JFrame/EXIT_ON_CLOSE)))
