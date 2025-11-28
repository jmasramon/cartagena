(ns cartagena.core)

;; Core game configuration and shared constants.
;;
;; This namespace defines the basic parameters of a Cartagena game
;; (available colors, card types, number of cards, etc.) that are
;; used across the data-abstraction layers (players, deck, board,
;; game state and moves).


;; Maximum number of players allowed in a game.
;; (def MAX-PLAYERS (count PIRATE-COLORS))


;; Number of cards each player gets.
(def NUM-CARDS 6)

;; Size of a freshly generated deck.
(def DECK-SIZE 50)

;; Default number of players if none is specified.
(def DEF-NUM-PLAYERS 3)

;; Number of actions a player receives at the start of their turn.
(def STARTING-ACTIONS 3)

;; Number of pieces each player starts with on the start square.
(def NUM-STARTING-PIECES NUM-CARDS)

;;
;; ABSTRACTION BARRIER DESIGN (following SICP principles):
;;
;; This project is organized in strict abstraction layers, where each layer
;; depends ONLY on the layer directly below it. Higher layers never directly
;; access lower-layer implementation details.
;;
;; Layer 0 (Foundation): Core constants and primitives
;;   - This namespace (cartagena.core)
;;   - Defines game parameters, no dependencies
;;
;; Layer 1 (Basic Data Structures): Individual game entities
;;   - cartagena.data-abstractions.square[-bis]
;;   - cartagena.data-abstractions.player[-bis]
;;   - cartagena.data-abstractions.deck
;;   Dependencies: Layer 0 only (core constants)
;;
;; Layer 2 (Composite Structures): Collections of Layer 1 entities
;;   - cartagena.data-abstractions.board
;;   Dependencies: Layer 0 (core) + Layer 1 (square, player)
;;
;; Layer 3 (Game State): Complete game representation
;;   - cartagena.data-abstractions.game
;;   Dependencies: Layer 0 (core) + Layer 1 (player, deck) + Layer 2 (board)
;;
;; Layer 4 (Game Rules): State transformation logic
;;   - cartagena.data-abstractions.moves
;;   Dependencies: Layer 3 (game) only - never directly touches board/player/deck
;;
;; Layer 5 (User Interface): Presentation and interaction
;;   - cartagena.swingUI.shaping (geometry)
;;   - cartagena.swingUI.drawing (rendering)
;;   - cartagena.swingUI.swingUI (event handling)
;;   Dependencies: Layer 0 (core) + Layer 1 (player, square) + Layer 2 (board)
;;                + Layer 3 (game) + Layer 4 (moves)
;;
;; The abstraction barrier ensures:
;;   1. Changes to lower layers don't affect higher layers (as long as API is stable)
;;   2. Alternative implementations (e.g., player-bis, square-bis) can be swapped
;;      without changing any code outside their own namespace
;;   3. Each layer presents a clean API hiding implementation details