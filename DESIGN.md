---
version: alpha
name: Baratie Heritage (Stitch Sync)
description: A specialized design system synchronized with the Google Stitch project, blending Sanji's "Black-Leg" suit aesthetic with Baratie's nautical luxury.
colors:
  primary: "#1A1A1B"    # Sanji Black (Suit-inspired)
  secondary: "#D4AF37"  # Baratie Gold (Golden finishes)
  tertiary: "#2C5F78"   # All Blue Ocean (Deep aquatic teal)
  surface: "#F5F5F0"    # Chef's Apron (Creamy off-white)
  on-surface: "#1A1A1B" # Absolute Professional Ink
  error: "#B22222"      # Diable Jambe Red (Deep crimson heat)
  accent-gradient: "linear-gradient(45deg, #B22222, #FF4500)" # Heating Leg Effect
typography:
  headline-display:
    fontFamily: Playfair Display
    fontSize: 3.5rem
    fontWeight: 900
  title-menu:
    fontFamily: Montserrat
    fontSize: 1.5rem
    fontWeight: 600
    letterSpacing: "0.1em"
    textTransform: uppercase
  body-readable:
    fontFamily: Lato
    fontSize: 1.125rem
    lineHeight: 1.6
rounded:
  menu-card: 8px
  action-pill: 9999px
elevation:
  premium: "0 2px 10px rgba(212, 175, 55, 0.15)" # Subtle Gold Glow
---

## Overview: The Black-Leg Aesthetic

This design system is directly synchronized with the **Google Stitch Project (4201767246920440368)**. It shifts the vibe from general nautical to a more tailored, sophisticated "Sanji-specific" look. It emphasizes the contrast between his sharp black suit and the warm, golden atmosphere of the Baratie.

## Colors: The Chef's Palette

- **Sanji Black (#1A1A1B):** The core of the identity. Used for high-prestige backgrounds and primary interactions.
- **Baratie Gold (#D4AF37):** Used for highlighting "Masterpiece" dishes and active navigation states.
- **Diable Jambe Red (#B22222):** Reserved for high-energy call-to-actions (like "Start the Service") and error states.

## Typography: Formal & Functional

We use **Cinzel** or **Playfair Display** for a high-end restaurant menu feel. Section headers use **Montserrat** in uppercase to maintain the sous-chef discipline. **Lato** provides the necessary clarity for long instruction lists.

## Component Refinements

- **Menu Cards**: Now feature a subtle 1px Gold border (`#D4AF37`) when a recipe is a "Favorite."
- **Diable Jambe Buttons**: Primary buttons feature a subtle heating gradient on press to simulate Sanji's signature attack.
- **Nautical Navigation**: Icons are gold on a dark background, evoking the night sky above the East Blue.
