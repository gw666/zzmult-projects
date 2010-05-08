(ns edu.umd.cs.piccolo.PCanvas.GraphEditor
 (:gen-class
   :extends edu.umd.cs.piccolo.PCanvas
   :state state
   :init init
   :post-init GraphEditorInit
   :constructors {[int int] []})
 (:import
   (java.awt Color Dimension)
   (edu.umd.cs.piccolo   PCanvas PLayer PNode PRoot)
   (edu.umd.cs.piccolo.nodes   PPath)
   (java.util   ArrayList Random)
   (javax.swing   JFrame)
   ))

(defn -init [width height]
  ; returns [ signature of superclass (no args), value of state (a map) ]
  [[] { :num-nodes 5, :num-edges 5, :random (Random.) }])

(defn add-to-node [node edge]
  (let [current-count (.getAttribute node "num-used")
        ]
    (aset (.getAttribute node "edges") current-count edge)
    (.addAttribute node "num-used" (inc current-count))))

(defn add-to-edge [edge node]
  (let [current-count (.getAttribute edge "num-used")
        ]
    (aset (.getAttribute edge "nodes") current-count node)
    (.addAttribute edge "num-used" (inc current-count))))

(defn -GraphEditorInit [this width height]
  (.setPreferredSize this (Dimension. width height))
  (let [{:keys [num-edges num-nodes random]} (.state this)
        nodeLayer (.getLayer this)
        edgeLayer (PLayer.)
        node-vector   ; its value is result of loop, on next line
        (loop [result [], x num-nodes]
          (if (zero? x)
            result   ; returned if true
            (recur (conj result (PPath/createEllipse
                                  (.nextInt random width)
                                  (.nextInt random height)
                                  20
                                  20))
              (dec x))))   ; returned if false
        n1 (first node-vector)
        ]

    (defn install-node [node]
      (println node)
      (.addChild nodeLayer node)
      (.addAttribute node "edges" (make-array PPath num-edges))
      (.addAttribute node "num-used" 0)
      )

    (.addChild (.getRoot this) edgeLayer)
    (.addLayer (.getCamera this) 0 edgeLayer)

    ;    (println "vector")
    (println node-vector)
    ;    (doall (map install-node node-vector))
    (doall (for [nv node-vector]
             (install-node nv)))

    ; the test below proves that add-to-node adds new PPaths correctly
    ; into the array edges, and that num-used can be updated by executing
    ; addAttribute a second time (no need for refs)
    (let [p1 (PPath/createEllipse 50 50 20 30)
          p2 (PPath/createRectangle 100 100 40 20)]
      (println (.getAttribute n1 "num-used"))
      (println p1)
      (println p2)
      (add-to-node n1 p1)
      (add-to-node n1 p2)
      (println (.getAttribute n1 "num-used"))
      ; should the same as p2
      (println (aget (.getAttribute n1 "edges") 1)))
    ))

(defn -main []
  (let [window (JFrame.)
        ge (edu.umd.cs.piccolo.PCanvas.GraphEditor. 500 500)]
    (println "... got to beginning of let")
    (doto window
      (.setTitle "Piccolo Graphics Editor")
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE))
    (.add (.getContentPane window) ge)
    (println "... got to adding Graphics Editor to window")
    (doto window
      (.pack)
      (.setVisible true))
    )

  (println "Goodbye!"))
