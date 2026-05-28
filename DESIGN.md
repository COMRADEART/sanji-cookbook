---
version: alpha
name: Baratie Heritage
description: A high-fidelity design system for Sanji's AI Chef Companion, blending nautical elegance with professional culinary precision.
colors:
  primary: "#0A2463"    # Nautical Deep Blue
  secondary: "#FFD700"  # Golden Baratie Accents
  tertiary: "#FB3640"   # Chef's Passion Red
  surface: "#F8F9FA"    # Clean Kitchen White
  on-surface: "#1D1E1F" # Professional Ink
  neutral: "#6C757D"    # Stainless Steel Gray
typography:
  headline-lg:
    fontFamily: Playfair Display
    fontSize: 2.5rem
    fontWeight: 700
    lineHeight: 1.2
  title-md:
    fontFamily: Montserrat
    fontSize: 1.25rem
    fontWeight: 600
  body-md:
    fontFamily: Inter
    fontSize: 1rem
    lineHeight: 1.5
rounded:
  sm: 4px
  md: 12px
  lg: 24px
spacing:
  xs: 4px
  sm: 8px
  md: 16px
  lg: 32px
components:
  recipe-card:
    backgroundColor: "{colors.surface}"
    rounded: "{rounded.md}"
    padding: "{spacing.md}"
  button-cook:
    backgroundColor: "{colors.tertiary}"
    textColor: "#FFFFFF"
    rounded: "{rounded.lg}"
    padding: 16px
  navigation-bar:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.secondary}"
---

## Overview

The Baratie Heritage design system is built to reflect Vinsmoke Sanji's dual nature: the disciplined, high-end professional chef and the passionate, high-energy pirate. It emphasizes clarity during the cooking process while maintaining a premium, "One Piece" cinematic aesthetic.

## Colors

- **Nautical Deep Blue (#0A2463):** Represents the ocean and the Baratie's roots. Used for primary navigation and branding.
- **Golden Accents (#FFD700):** Symbolizes the "Gold" in Baratie's legacy. Used for high-emphasis highlights and icons.
- **Chef's Passion Red (#FB3640):** A vibrant red used for action buttons (like "Start Cooking") and emotional triggers.

## Typography

We use **Playfair Display** for headlines to evoke a sense of high-end restaurant menus. **Montserrat** and **Inter** are used for titles and body text to ensure maximum readability in a busy kitchen environment.

## Layout

The layout follows a "Grid-First" approach, optimized for both mobile viewing and tablet "countertop" use. Spacing is generous to prevent accidental taps when the user's hands are busy.

## Shapes

Rounding is a key element of the identity. Cards use a modern `12px` radius, while high-action items like the "Start Cooking" button use a pill-shaped `24px` radius for a friendly, approachable feel.

## Components

- **`recipe-card`**: Uses a clean surface with subtle elevation to make the AI-generated "Living Dish" previews pop.
- **`button-cook`**: High-contrast red button designed to be the single most important action on any recipe screen.
- **`navigation-bar`**: A deep blue anchor for the application, providing consistent access to Sanji's "Brain" and the kitchen tools.

## Do's and Don'ts

- **DO** use dynamic motion for dish previews.
- **DO** maintain high contrast between text and backgrounds for accessibility.
- **DON'T** use cluttered layouts that make it hard to read instructions while cooking.
- **DON'T** waste a single pixel of space on unnecessary decorations—every element should serve a purpose.
