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
   (java.awt   BasicStroke Font GraphicsEnvironment Rectangle)
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

            ; how to get the font of a text node
            font (.getFont cardText)

            ; how to create a new font
            font2 (Font. "Monospaced", Font/PLAIN, 14)

            ; see commented line below: how to change size of existing font
            ;font2 (.deriveFont font (float 18))
            ]
        (.setFont cardText font2)

        ; set cardBorder to enclose cardText node
        (let [cardTextBounds (.getGlobalBounds cardText)]
          (.inset cardTextBounds -14 -14)  ; give it some border space
          (.setBounds cardBorder cardTextBounds))

        ; create clip border thick enough to be grabbed with mouse pointer
        (.setStroke clipRect (BasicStroke. (float 4)))
        (.setPathToRectangle clipRect 0 0 300 18)

        ; set hierarchy of layer to clip, border, and text nodes
        (.addChild clipRect cardBorder)
        (.addChild clipRect cardText)
        (.addChild thisLayer clipRect)

        ; ensure that clip, border, and text move together
        (.setChildrenPickable clipRect false)

        ; install basic drag event handler, disable default pan handler
        (.. this getCanvas (addInputEventListener (PDragEventHandler.)))
        ; without next line, mouse drag on one node will cause movement of node
        ; plus pan of canvas, leading to illusion two nodes are linked
        ; by some scaling factor
        (.. this getCanvas (setPanEventHandler nil))
;        (.animateToPositionScaleRotation cardText 100 50 1 0 0)




        ; how to print the font used by cardText node
        ;(println (.toString (.getFont cardText)))

        ; simple experiments with coordinate systems
        (println (.toString (.getBounds cardText)))
        (println (.getOrigin (.getBounds cardText)))
;        (println (.localToGlobal (.getOrigin (.getBounds cardText))))
        ;(println (.. localToGlobal getOrigin getBounds cardText))
        ))))

(defn -main []
  (let [main-frame (create-frame)]
    (.setVisible main-frame true)))
