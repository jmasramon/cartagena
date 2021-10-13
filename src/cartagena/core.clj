(ns cartagena.core)

;; Constants
(def pirate-colors #{:red :green :yellow}) ;; TODO: there should be more colors to choose from
(def card-types #{:hat :flag :pistol :sword :bottle :keys})
(def num-cards (count card-types))
(def deck-size 50)
(def def-num-players 3)
(def starting-actions 3)
(def num-starting-pieces 6)

;; DATA ABSTRACTIONS

;; -- moves --  ; change the game
;; -- game --   ; contains players, cards, and board
  ;; -- player --

  ;; -- deck --
    ;; -- cards --  

  ;; -- board --
    ;; -- square --