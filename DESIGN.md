---
version: alpha
name: Baratie Heritage V2
description: A premium, AI-native design system generated via Google Stitch paradigms. It blends "Nautical Luxury" with "Sous Chef Discipline."
colors:
  primary: "#05163D"    # Midnight Atlantic (Slightly deeper, more luxurious)
  secondary: "#D4AF37"  # Metallic Baratie Gold (Sophisticated texture)
  tertiary: "#D72638"   # Crimson Passion (Refined high-action red)
  surface: "#FFFFFF"    # Pristine Kitchen White
  surface-dim: "#F4F7F9" # Soft Stainless Gray (For subtle depth)
  on-surface: "#0D0E10" # Absolute Professional Ink
  glass: "rgba(255, 255, 255, 0.7)" # Glassmorphism base
typography:
  headline-display:
    fontFamily: Playfair Display
    fontSize: 3.25rem
    fontWeight: 900
    lineHeight: 1.1
    letterSpacing: "-0.01em"
  title-premium:
    fontFamily: Montserrat
    fontSize: 1.5rem
    fontWeight: 700
    letterSpacing: "0.05em"
  body-lux:
    fontFamily: Inter
    fontSize: 1.125rem
    lineHeight: 1.6
rounded:
  xs: 2px
  sm: 8px
  md: 16px
  lg: 32px
  full: 9999px
elevation:
  soft: "0 4px 20px rgba(0, 0, 0, 0.05)"
  deep: "0 10px 40px rgba(0, 0, 0, 0.12)"
spacing:
  comfortable: 24px
  luxurious: 48px
components:
  living-dish-card:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.md}"
    elevation: "{elevation.soft}"
  button-action-pill:
    backgroundColor: "{colors.tertiary}"
    textColor: "#FFFFFF"
    rounded: "{rounded.full}"
    padding: "16px 32px"
---

## Overview: The "Baratie Vibe"

In V2, we transition from functional to **Cinematic**. The interface should feel like a high-end restaurant menu in the middle of a calm ocean. We use "Vibe Design" to ensure Sanji's personality permeates every pixel—sophisticated, disciplined, and slightly dramatic.

## Colors: Nautical Luxury

- **Midnight Atlantic (#05163D):** This is the color of the deep sea at night. It provides a luxurious, high-contrast anchor for the entire UI.
- **Metallic Baratie Gold (#D4AF37):** Not just a color, but a symbol of the "Chef's Treasure." Use this sparingly for icons, highlights, and prestige elements.
- **Crimson Passion (#D72638):** A more muted, professional red that signals "Action" without being overwhelming.

## Typography: The High-End Menu

We lean into **Playfair Display (900)** for Display text to create a "Gourmet" feel. **Montserrat** is always uppercase for titles to signify professional discipline. **Inter** handles the heavy lifting of instruction reading with generous line height (`1.6`).

## Layout: The Infinite Service

The layout uses **Luxurious Spacing (48px)** for section headers and **Comfortable Spacing (24px)** for card internals. This ensures the user never feels rushed or cluttered while cooking.

## Components: Living Assets

- **`living-dish-card`**: Designed to frame the **Nano Banana** motion assets. It uses a very soft shadow (`elevation.soft`) to feel like it's floating above the stainless steel kitchen surface.
- **`button-action-pill`**: A full-pill shape that feels tactile and easy to hit in a messy kitchen.

## Do's and Don'ts

- **DO** use subtle gradients and glassmorphism for "Floating" elements (like Sanji's chat bubbles).
- **DO** describe "Smooth Motion" (parallaxes, fades) in every new UI task.
- **DON'T** use sharp corners—everything should feel organic and fluid like the ocean.
- **DON'T** use generic system fonts for branding; lean into the specific typography tokens.
