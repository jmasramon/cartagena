(ns cartagena.core)

;; Constants
(def pirate-colors #{:red :green :yellow}) ;; TODO: there should be more colors to choose from
(def card-types #{:hat :flag :pistol :sword :bottle :keys})
(def num-cards (count card-types))
(def deck-size 50)
(def def-num-players 3)
(def starting-actions 3)
(def starting-pieces 6)


;; Data structures
;; example of full game data (state of the game)
;; {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
;;            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]
;;  :turn-order {:green :red, :red :yellow, :yellow :green}  ;; TODO: turn-order never changes, should not be part of the state
;;  :turn :green
;;  :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
;;          {:pieces {:green 6, :red 6, :yellow 0}, :type :flag}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
;;  :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]}

;; DATA ABSTRACTIONS


;; -- moves --  ; change the game
;; -- game --   ; contains players, cards, and board
  ;; -- player --

  ;; -- deck --
    ;; -- cards --  

  ;; -- board --
    ;; -- square --