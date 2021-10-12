(ns cartagena.data-abstractions.player-bis
  (:require
   [cartagena.core :refer [starting-actions card-types]]
   [cartagena.data-abstractions.deck :refer [random-card add-card]]))

;; Functions that need to know how player is implemented
;; player:
;;  {color, cards, actions} 
;; ex:    {:color :yellow, :cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}
;; Internal entities (concepts) are:
;;   color  (constant; unchangeable)
;;   actions 
;;   cards

(defn make-player
  "Make a player with a color and a list of cards. It has 3 actions"
  ([color cards]
   (make-player color cards starting-actions))
  ([color cards actions]
   {:color color :actions actions, :cards cards}))

;; getters
(defn color
  "Get the color of the player"
  [player]
  (:color player))

(defn actions
  "Get the actions of the player"
  [player]
  (:actions player))

(defn cards
  "Get the cards of the player"
  ([player]
   (:cards player)))

;; testers
(defn player-has-card?
  "Does the player have a card?"
  [player card]
  (= card
     (some #{card} (cards player))))

;; setters
(defn set-cards
  ""
  [player cards]
  (assoc-in player [:cards] cards))

(defn set-actions
  ""
  [player actions]
  (assoc-in player [:actions] actions))

(defn add-random-card-to-player-in-players
  ""
  [player]
  (let [cards (cards player)
        new-card (random-card card-types)
        new-cards (add-card cards new-card)]
    (set-cards player new-cards)))

(defn reset-actions
  "Puts actions back to 3"
  [player]
  (assoc-in player [:actions] starting-actions))

(defn decrease-actions
  "Takes one action from the player"
  [player]
  (update-in player [:actions] dec))



