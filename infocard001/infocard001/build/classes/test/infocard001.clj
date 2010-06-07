(ns test.infocard001
 (:gen-class)
 (:import (edu.umd.cs.piccolo PCanvas PNode PLayer)
   (edu.umd.cs.piccolo.nodes   PPath PText)
   (edu.umd.cs.piccolo.event   PBasicInputEventHandler PDragEventHandler
     PDragSequenceEventHandler PInputEvent PInputEventFilter PPanEventHandler
     PZoomEventHandler)
   (edu.umd.cs.piccolo.util   PBounds)
   (edu.umd.cs.piccolox PFrame)
   (edu.umd.cs.piccolox.nodes PClip)
   (java.awt.geom   Dimension2D Point2D)
   (java.awt   Font GraphicsEnvironment Rectangle)
   ))

(defn create-frame
  "Creates the main PFrame used by the program."
  []
  (proxy [PFrame] []
    (initialize []
      (let [t (str "Slow down your day\n\n"
                "We are at our least effective when we act in reaction to \n"
                "whatever was the most recent thought in our head. When the \n"
                "brain is very active, it spins from idea to idea with little\n"
                "sense of connection between the two. Calming the mind becomes \n"
                "necessary before we can hope to have any sense of mastery \n"
                "over how we spend our time."
                )
            cardText (PText. t)
            cardBorder (PPath/createRectangle 0 0 100 10)
            clipRect (PClip.)
            thisLayer (.. this getCanvas getLayer)
            font (.getFont cardText)
            font2 (Font. "Monospaced", Font/PLAIN, 14)
            ; shows how to change size of an existing font
            ;font3 (.deriveFont font (float 18))
            ]
        (.setFont cardText font2)
        (let [cardTextBounds (.getGlobalBounds cardText)]
          (.inset cardTextBounds -5 -5)
          (.setBounds cardBorder cardTextBounds))
        (.setPathToRectangle clipRect 0 0 500 40)

        (.addChild cardBorder cardText)
        ;(.addChild clipRect cardBorder)
        (.addChild thisLayer cardBorder)
        ;(.addChild thisLayer clipRect)

        (.setChildrenPickable cardBorder false)
        ;(.setChildrenPickable clipRect false)

        (.. this getCanvas (addInputEventListener (PDragEventHandler.)))
        ; without next line, mouse drag on one node will cause movement of node
        ; plus pan of canvas, leading to illusion two nodes are linked
        ; by some scaling factor
        (.. this getCanvas (setPanEventHandler nil))
;        (.animateToPositionScaleRotation cardText 100 50 1 0 0)




        (println (.toString (.getFont cardText)))

        ;(println (.toString (.getBounds cardText)))
        ;(println (.getOrigin ))
        ;(println (.localToGlobal (.getOrigin (.getBounds cardText))))
        ;(println (.. localToGlobal getOrigin getBounds cardText))
        ))))

(defn -main []
  (let [main-frame (create-frame)]
    (.setVisible main-frame true)))
