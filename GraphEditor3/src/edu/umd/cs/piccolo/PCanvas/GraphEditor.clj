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
  [[] { :numNodes 50, :numEdges 50, :random (Random.) }])

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
  (let [{:keys [numEdges numNodes random]} (.state this)
        nodeLayer (.getLayer this)
        edgeLayer (PLayer.)
        node1 (PPath/createEllipse 40 60 20 20)
        node2 (PPath/createEllipse 140 160 20 20)
        edge1 (PPath.)
        ]
    (.addChild (.getRoot this) edgeLayer)
    (.addLayer (.getCamera this) 0 edgeLayer)
    (.addAttribute node1 "edges" (make-array PPath numEdges))
    (.addAttribute node1 "num-used" (ref 0))
    (.addAttribute edge1 "nodes" (make-array PPath numNodes))
    (.addAttribute edge1 "num-used" (ref 0))

    (add-to-node node1 edge1)
    (println @(.getAttribute node1 "num-used"))

    (add-to-edge edge1 node1)
    (add-to-edge edge1 node2)
    (println @(.getAttribute edge1 "num-used"))

    (doto nodeLayer
      (.addChild node1)
      (.addChild node2)
      )))

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





