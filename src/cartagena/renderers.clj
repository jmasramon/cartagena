(ns cartagena.renderers)

; All output side-effects (printing to screen) done here

(defn calculate-join-position [section]
	(let [types (for [elem section]
					(name (get-in elem [:type])))]
		(println "*** types:" types)
		(+ 28 (reduce + (map count types)))))

(defn render-section [section]
	(for [elem section] 
		(print (elem :type) "-- "))
	)

(defn render-join [position]
	;  
	(let [spaces (apply str (repeat position " "))]
		(dotimes [n 3] (do
						(print spaces) 
						(print "|")
						(println))
		)))