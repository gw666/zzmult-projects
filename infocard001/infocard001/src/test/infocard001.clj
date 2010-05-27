(ns test.infocard001
 (:gen-class)
 (:import (edu.umd.cs.piccolo PCanvas PNode PLayer)
   (edu.umd.cs.piccolo.nodes   PPath PText)
   (edu.umd.cs.piccolo.event   PBasicInputEventHandler PDragEventHandler
     PDragSequenceEventHandler PInputEvent PInputEventFilter PPanEventHandler
     PZoomEventHandler)
   (edu.umd.cs.piccolo.util   PBounds)
   (java.awt.geom   Dimension2D Point2D)
   (java.awt   Rectangle)
   (edu.umd.cs.piccolox PFrame)))

(defn create-frame
  "Creates the main PFrame used by the program."
  []
  (proxy [PFrame] []
    (initialize []
      (let [t (str "Slow down your day\n\n"
                "We are at our least effective when we act in reaction to \n"
                "whatever was the most recent thought in our head When the \n"
                "brain is very active, it spins from idea to idea with little\n"
                "sense of connection between the two. Calming the mind becomes \n"
                "necessary before we can hope to have any sense of mastery \n"
                "over how we spend our time."
                )
            aNode (PText. t)
            aNode2 (PPath/createRectangle 0 0 10 10)
            thisLayer (.. this getCanvas getLayer)
            ]
        (.. this getCanvas (addInputEventListener (PDragEventHandler.)))
        ; without this, mouse drag on one node will cause movement of node
        ; plus pan of canvas, leading to illusion two nodes are linked
        ; by some scaling factor
        (.. this getCanvas (setPanEventHandler nil))
        (.animateToPositionScaleRotation aNode 100 50 1 0 0)
        (let [aNodeBounds (.getGlobalBounds aNode)
              ]
          (.inset aNodeBounds -5 -5)
          (.setBounds aNode2 aNodeBounds)
          (.setChildrenPickable aNode2 false))

        (.addChild thisLayer aNode2)
        (.addChild aNode2 aNode)
        ;(println (.toString (.getBounds aNode)))
        ;(println (.getOrigin ))
        ;(println (.localToGlobal (.getOrigin (.getBounds aNode))))
        ;(println (.. localToGlobal getOrigin getBounds aNode))
        ))))

(defn -main []
  (let [main-frame (create-frame)]
    (.setVisible main-frame true)))
