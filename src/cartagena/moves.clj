(ns cartagena.moves
	(:require [cartagena.setters.sdariEntities :refer [add-piece remove-piece]]
				[cartagena.getters.mainEntities :refer [board]]
				))
; TODO: move the empty slot finders to the proper file
(defn empty-slot
	([slot card color]
	(and
		(= (slot :type) card)
		(= 0 ((slot :pieces) color))
	))
	([slot card]
	(and
		(= (slot :type) card)
		(= 0 (count (filter #(not= 0 (val %)) (slot :pieces))))
	))
)

(defn find-empty-slot
	([game-state card origin color]
	"first slow after origin, of same type as card and with nobody of same color there"
	; (println "find-empty-slot with card:" card "origin:" origin)
	(let [
		board (board game-state)
		subBoard (subvec board (inc origin))]
		; (first (filter f coll)
		(+ (inc origin) (first (keep-indexed #(if (empty-slot %2 card color) %1) subBoard)))
	))
	([game-state card origin]
	"first slow after origin, of same type as card and with nobody there"
	; (println "find-empty-slot with card:" card "origin:" origin)
	(let [
		board (board game-state)
		subBoard (subvec board (inc origin))
		sameCardIndexes (keep-indexed #(if (empty-slot %2 card) %1) subBoard)]
		; (println "sameCardIndexes" sameCardIndexes "of len" (count sameCardIndexes))
		(if (> (count sameCardIndexes) 0)
			(+ (inc origin) (first sameCardIndexes))
			(dec (count board)))
	))
)

(defn nonempty-slot [slot]
	(not= 0 (count (filter #(not= 0 (val %)) (slot :pieces))))
)

(defn find-nonempty-slot [game-state origin]
	(let [
		board (board game-state)
		subBoard (subvec board 0 origin)
		nonemptySlotsIndexes (keep-indexed #(if (nonempty-slot %2) %1) subBoard)]
		(if (> (count nonemptySlotsIndexes) 0)
			(last nonemptySlotsIndexes)
			0)
	)
)

(defn move-piece [board from to color]
	; (println "move-piece with from:" from "to:" to "color:" color)
	(let [board' (remove-piece board from color)]
		(add-piece board' to color)))
