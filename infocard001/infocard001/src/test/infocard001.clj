; file: infocard001.clj
; last changed: 6/13/10

; history:

; 6/13/10:
; GOAL: Initial attempt to make an infocard that can be moved around the
; screen, with the card normally showing the title only--switching to showing
; title + contents when the mouse pointer moves over the card.

; IMPLEMENTATION: To use PClip to show title only OR title plus contents,
; implemented by changing the bounds of the clip.
;
; STATUS: Compiles and runs OK.
; Unexpected difficulty in moving the clip; user must grab the exact
; boundary of the clip--too difficult. For now, halted in favor of exploring
; alternative implementation.
 

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
      ; sample infocard
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

        ; install basic drag event handler
        (.. this getCanvas (addInputEventListener (PDragEventHandler.)))

        ; without next line, mouse drag on one node will cause movement of node
        ; plus pan of canvas, leading to illusion two nodes are linked
        ; by some scaling factor
        (.. this getCanvas (setPanEventHandler nil))
;        (.animateToPositionScaleRotation cardText 100 50 1 0 0)




        ; how to print the font used by cardText node
        ;(println (.toString (.getFont cardText)))

        ; simple experiments with coordinate systems
        ; doesn't work--localToGlobal requires a rectangle
        ;(println (.toString (.getBounds cardText)))
        ; returns #<Double Point2D.Double[0.0, 0.0]>
        ;(println (.getOrigin (.getBounds cardText)))
        ;(println (.localToGlobal (.getOrigin (.getBounds cardText))))
        ;(println (.. localToGlobal getOrigin getBounds cardText))
        ))))

(defn -main []
  (let [main-frame (create-frame)]
    (.setVisible main-frame true)))
