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
  [[] { :num-nodes 5, :num-edges 5, :random (Random.) }])

(defn add-to-node [node edge]
  (let [num-ref (.getAttribute node "num-used")
        current-count @num-ref]
    (dosync
      (aset (.getAttribute node "edges") current-count edge)
      (alter num-ref inc))))

(defn add-to-edge [edge node]
  (let [num-ref (.getAttribute edge "num-used")
        current-count @num-ref]
    (dosync
      (aset (.getAttribute edge "nodes") current-count node)
      (alter num-ref inc))))

(defn -GraphEditorInit [this width height]
  (.setPreferredSize this (Dimension. width height))
  (let [{:keys [num-edges num-nodes random]} (.state this)
        nodeLayer (.getLayer this)
        edgeLayer (PLayer.)
        node-ref-vector (take num-nodes
    ; error: repeats the same ref over and over
                          (repeat (ref (PPath/createEllipse
                                    (.nextInt random width)
                                    (.nextInt random height)
                                    20
                                    20))))
        ]

    ; error: no nodes are displayed in window
    (defn install-node [node]
      (.addAttribute node "edges" (make-array PPath num-edges))
      (.addAttribute node "num-used" (ref 0))
      (.addChild nodeLayer node)
      node)

    (.addChild (.getRoot this) edgeLayer)
    (.addLayer (.getCamera this) 0 edgeLayer)

    (println node-ref-vector)

    ; error: this doesn't print anything!
    (for [node-ref node-ref-vector]
      (do
      (println node-ref)
      (dosync
        (alter node-ref install-node)))
      )

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





