/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.mytests;

import javax.swing.JComponent;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;

/**
 *
 * @author soura
 */
public class MyStyleFactory extends SynthStyleFactory {
        protected static String variant = "regular";

        final SynthStyleFactory styleFactory = SynthLookAndFeel.getStyleFactory();

        static {
            SynthLookAndFeel.setStyleFactory(new MyStyleFactory(variant));
        }

        public MyStyleFactory(String variant) {
            if (variant.equals("regular") || variant.equals("mini")
                    || variant.equals("small") || variant.equals("large"))
                MyStyleFactory.variant = variant;
        }

        @Override
        public SynthStyle getStyle(JComponent c, Region id) {
            c.putClientProperty("JComponent.sizeVariant", variant);
            return styleFactory.getStyle(c, id);
        }
	}
