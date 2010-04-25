(comment
 GraphEditor
 100424 NOTE: I've stopped development here because I realized that
 GraphEditor adds new method, which means it must be implemented using
 gen-class. This is a snapshot of what I've accomplished so far.
)

(ns grapheditor
  (:gen-class)
  (:import
    (java.awt   Color Dimension)
    (java.awt.event   InputEvent MouseEvent)
    (java.awt.geom   Point2D)
    (javax.swing   JFrame)
    (java.util   ArrayList Random)
    (edu.umd.cs.piccolo   PCanvas PLayer PNode)
    (edu.umd.cs.piccolo.nodes   PPath)
    (edu.umd.cs.piccolo.event   PDragEventHandler PInputEvent PInputEventFilter)
    ))

(defn make-graph-editor [width height]
  (let [graph-editor (proxy [PCanvas] [])
        nodeLayer (.getLayer graph-editor)
        edgeLayer (PLayer.)
        ]
    (doto graph-editor
      (.setPreferredSize (Dimension. width height))
      (.. getRoot (addChild edgeLayer))
      (.. getCamera (addLayer 0 edgeLayer))
      (.. nodeLayer (addChild (PPath/createEllipse 50 50 20 20)))
      )))

(defn make-graph-editor-tester []
  (let [graphEditor (make-graph-editor 500 500)]
    (doto graphEditor
      (.setTitle "Piccolo Graph Editor")
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.. getContentPane (add graphEditor))
      (.pack)
      (.setVisible true))))

(defn -main []
  (make-graph-editor-tester))
