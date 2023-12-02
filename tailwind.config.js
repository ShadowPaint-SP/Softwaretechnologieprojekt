/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html"
  ],
  theme: {
    fontSize: {
      sm: '0.750rem',
      base: '1rem',
      xl: '1.333rem',
      '2xl': '1.777rem',
      '3xl': '2.369rem',
      '4xl': '3.158rem',
      '5xl': '4.210rem',
    },
    fontFamily: {
      heading: 'Poppins',
      body: 'Poppins',
    },
    fontWeight: {
      normal: '400',
      bold: '700',
    },
    extend: {},
  },
  plugins: [require("daisyui")],
  daisyui: {
    themes: [
      {
        light: {
          "primary": "#e4cb4e",
          "secondary": "#d7e7b6",
          "accent": "#7a9e33",
          "neutral": "#0b0504",
          "base-100": "#fbf7f4",
          "info": "#00a1d2",
          "success": "#40fe91",
          "warning": "#ffcf00",
          "error": "#ff6973",
        },
        dark: {
          "primary": "#b1981b",
          "secondary": "#394918",
          "accent": "#a8cc61",
          "neutral": "#fbf5f4",
          "base-100": "#1a1814",
          "info": "#00a1d2",
          "success": "#40fe91",
          "warning": "#ffcf00",
          "error": "#ff6973",
        },
      },
    ],
  },
}

