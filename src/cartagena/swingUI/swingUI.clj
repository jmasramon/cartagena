(ns cartagena.swingUI.swingUI
  (:require [cartagena.data-abstractions.moves :as mvs]
            [cartagena.data-abstractions.game :as g]
            [cartagena.swingUI.shaping :refer [track-shapes]]
            [cartagena.swingUI.drawing :as dw :refer [draw-cards draw-pieces]])

  (:import (java.awt BorderLayout Component Shape)
           (javax.swing JFrame JPopupMenu JMenuItem JOptionPane JLabel)
           (java.awt.event MouseAdapter ActionListener MouseEvent)))

;; ABSTRACTION LAYER: Layer 5 (User Interface - Event Handling)
;;
;; This namespace handles user interactions and delegates to game logic.
;; It depends on:
;;   - Layer 0: cartagena.core (for pirate-colors)
;;   - Layer 3: cartagena.data-abstractions.game (for game state queries)
;;   - Layer 4: cartagena.data-abstractions.moves (for game actions)
;;   - Layer 5: cartagena.swingUI.shaping (for coordinate mapping)
;;              cartagena.swingUI.drawing (for rendering)
;;
;; CRITICAL: This layer never directly manipulates board, player, deck, or
;; square structures. All game operations go through the moves and game APIs.
;;
;; Main Swing UI wiring for Cartagena.
;;
;; This namespace owns the window, event handling and connection between
;; user interactions and the pure game logic (cartagena.data-abstractions
;; namespaces). It:
;;   - builds the main JFrame and drawing component
;;   - maps mouse clicks to board squares via track-shapes
;;   - shows context menus (Play card / Fall Back / Pass) and delegates
;;     to the moves layer
;;   - triggers repaints when the game atom changes

(defn create-action
  "Create an ActionListener that runs action and repaints the parent."
  [parent action]
  (proxy [ActionListener] []
    (actionPerformed [_]
      (action)
      (.repaint parent))))

(defn popup
  "Show a context menu for the clicked square, wiring menu items to moves.
   The menu includes options to play a card, fall back, or pass."
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

(defn get-click-index
  "Return the index of the board square at screen coordinates (x, y), or nil."
  [x y]
  (first (filter #(.contains ^Shape (get track-shapes %) x y) (range (count track-shapes)))))

(defn clicker
  "Create a MouseAdapter that reacts to clicks by opening the popup menu."
  [parent game]
  (let [click-handler #(when-let [clicked (get-click-index (-> % .getPoint .x) (-> % .getPoint .y))]
                         (popup parent %
                                game clicked))]
    (proxy [MouseAdapter] []
      (mousePressed [event] (click-handler event))
      (mouseReleased [event] (click-handler event)))))

(defn draw-board
  "Create the Swing component responsible for drawing the current game."
  [game]
  (let [component (proxy [Component] []
                    (paint [graphics]
                      (doto graphics
                        (dw/draw-board (g/board @game))
                        (draw-cards (g/active-player @game))
                        (draw-pieces (g/board @game)))))]
    (.addMouseListener component (clicker component game))
    component))

(defn frame
  "Create and show the main game window for the given initial game state."
  [initial-game-state exit-condition]
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

(defn -main
  "Entry point. Ask for number of players, then start a new game window."
  []
  (let [num-players (JOptionPane/showInputDialog nil "Enter players (2-5):")]

    (frame
     (g/make-game (read-string num-players))
     JFrame/EXIT_ON_CLOSE)))
