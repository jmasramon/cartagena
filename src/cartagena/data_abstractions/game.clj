(ns cartagena.data-abstractions.game)

;; Functions that need to know how game-state is implemented

(defn players [game-state]
  (game-state :players))

(defn turn-order [game-state]
  (game-state :turn-order))

(defn turn [game-state]
  (game-state :turn))

(defn nex-turn [game-state]
  ((turn game-state) (turn-order game-state)))

(defn board [game-state]
  (game-state :board))

(defn deck [game-state]
  (game-state :deck))

(defn set-players [game-state players]
  (assoc-in game-state [:players] players))

(defn set-turn-order [game-state turn-order]
  (assoc-in game-state [:turn-order] turn-order))

(defn set-turn [game-state turn]
  (assoc-in game-state [:turn] turn))

(defn set-board [game-state board]
  (assoc-in game-state [:board] board))

(defn set-deck [game-state deck]
  (assoc-in game-state [:deck] deck))