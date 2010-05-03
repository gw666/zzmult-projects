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
   (java.util   Random)
   (javax.swing   JFrame)
   ))

(defn -init [width height]
  [[] { :numNodes 50, :numEdges 50, :random (Random.) }])

(defn -GraphEditorInit [this width height]
  (.setPreferredSize this (Dimension. width height))
  (let [{:keys [numEdges numNodes random]} (.state this)
        nodeLayer (.getLayer this)
        edgeLayer (PLayer.)
        node (PPath/createEllipse 40 60 20 20)
        ]
    (.addChild (.getRoot this) edgeLayer)
    (.addLayer (.getCamera this) 0 edgeLayer)
    (.addChild nodeLayer node)
    (println (.nextInt random numNodes))
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





