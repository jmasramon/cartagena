(ns cartagena.data-abstractions.moves
  (:require
   [cartagena.data-abstractions.game :as g]))

;; ABSTRACTION LAYER: Layer 4 (Game Rules)
;;
;; This namespace implements the game rules as pure transformations of game state.
;; It depends ONLY on:
;;   - Layer 3: cartagena.data-abstractions.game (game state operations)
;;
;; CRITICAL ABSTRACTION BARRIER: This layer never directly accesses board, player,
;; deck, or square namespaces. All game state manipulation goes through the game
;; namespace API. This ensures that:
;;   1. Game rules are expressed at the right level of abstraction
;;   2. Changes to lower layers don't break game logic
;;   3. The rules remain readable and maintainable
;;
;; Move primitives for the game.
;;
;; A "move" here is a rule that transforms the immutable game state in
;; response to a player's action. Each player has a limited number of
;; actions (see cartagena.core/starting-actions) on their turn.
;;
;; High-level effects of moves:
;;   - Decrease the active player's remaining actions.
;;   - Move pieces forward or backward on the board.
;;   - Add/remove cards from the active player's hand.
;;   - Change the active player when a turn ends; the new active
;;     player receives a fresh set of actions.
;;
;; Invariants these functions rely on / help enforce:
;;   - Where a played card causes a piece to land.
;;   - Where a fall-back move lands a piece.
;;   - How many pieces may occupy a fall-back destination square.

(defn pass
  "Consume one action from the active player.
   If this was the last action, advance the turn to the next player
   and reset their actions."
  [game]
  (g/turn-played game))

(defn fall-back
  "Execute a fall-back move for the active player.
   1. Move a piece back to the closest non-empty slot behind it.
   2. Add a random card to the active player's hand.
   3. Consume one action (and possibly pass the turn)."
  [game from]
  (let [to (g/fallback-square-index game from)]
    (if (and
         (g/available-piece? game from)
         (g/available-fall-back? game from))
      (-> (g/move-piece game from to)
          g/add-random-card-to-active-player
          g/turn-played)
      game)))

(defn play-card
  "Play a card for the active player.
   1. Use the given card to move a piece from its current position to
      the next empty slot of the same type (or to the boat if none).
   2. Remove the used card from the active player's hand.
   3. Consume one action (and possibly pass the turn)."
  [game card from]
  (let [to (g/next-empty-slot-index game card from)]
    (if (g/available-piece? game from)
      (-> (g/move-piece game from to)
          (g/remove-played-card card)
          g/turn-played)
      game)))
