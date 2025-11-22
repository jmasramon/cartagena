(ns cartagena.data-abstractions.player-bis
  (:require
   [cartagena.core :refer [starting-actions card-types]]
   [cartagena.data-abstractions.deck :refer [random-card add-card]]))

;; ABSTRACTION LAYER: Layer 1 (Basic Data Structures)
;;
;; This namespace represents a player as an individual game entity.
;; It depends ONLY on:
;;   - Layer 0: cartagena.core (for starting-actions and card-types constants)
;;              cartagena.data-abstractions.deck (for card operations)
;;
;; Higher layers (board, game, UI) interact with players through this
;; namespace's public API, never directly accessing player internals.
;;
;; Alternative player representation used to validate the data abstraction
;; barrier. This namespace defines a different concrete shape for "player"
;; (a map with :color, :cards and :actions) while keeping the same public
;; API as cartagena.data-abstractions.player. The rest of the code and tests
;; should work unchanged when switching between these implementations.
;;
;; Functions that need to know how player is implemented
;; player:
;;  {color, cards, actions} 
;; ex:    {:color :yellow, :cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}
;; Internal entities (concepts) are:
;;   color  (constant; unchangeable)
;;   actions 
;;   cards

;; builders
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
  "Return a new player with the given sequence of cards."
  [player cards]
  (assoc player :cards cards))

(defn set-actions
  "Return a new player with the given remaining number of actions."
  [player actions]
  (assoc player :actions actions))

(defn add-random-card-to-player-in-players
  "Return a new player with one additional random card drawn from card-types."
  [player]
  (let [cards (cards player)
        new-card (random-card card-types)
        new-cards (add-card cards new-card)]
    (set-cards player new-cards)))

(defn reset-actions
  "Puts actions back to 3"
  [player]
  (assoc player :actions starting-actions))

(defn decrease-actions
  "Takes one action from the player"
  [player]
  (update-in player [:actions] dec))



