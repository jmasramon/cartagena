(ns cartagena.core)

;; Core game configuration and shared constants.
;;
;; This namespace defines the basic parameters of a Cartagena game
;; (available colors, card types, number of cards, etc.) that are
;; used across the data-abstraction layers (players, deck, board,
;; game state and moves).

;; Available player colors.
(def pirate-colors #{:red :green :yellow :blue :brown})

;; Card types used both on the board and in players' hands.
(def card-types #{:hat :flag :pistol :sword :bottle :keys})

;; Number of distinct card types.
(def num-cards (count card-types))

;; Size of a freshly generated deck.
(def deck-size 50)

;; Default number of players if none is specified.
(def def-num-players 3)

;; Number of actions a player receives at the start of their turn.
(def starting-actions 3)

;; Number of pieces each player starts with on the start square.
(def num-starting-pieces 6)

;;
;; DATA ABSTRACTION LAYERS (see corresponding namespaces):
;;   - deck / cards
;;   - player
;;   - square / board
;;   - game
;;   - moves (rules that transform game state)