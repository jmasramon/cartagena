(ns cartagena.core)

;; Constants
(def pirate-colors #{:red :green :yellow :blue :brown}) 
(def card-types #{:hat :flag :pistol :sword :bottle :keys})
(def num-cards (count card-types))
(def deck-size 50)
(def def-num-players 3)
(def starting-actions 3)
(def num-starting-pieces 6)

;; DATA ABSTRACTIONS

;; -- moves --  ; change the game through player actions
;; -- game --   ; contains players, cards, and board. Also keeps track of active-player, its available moves and turn-order
  ;; -- player --

  ;; -- deck --
    ;; -- cards --  ;;just a keyword so not really new data

  ;; -- board --
    ;; -- square --