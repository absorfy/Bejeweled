// src/effects/gemEffects.js

const baseGlow = (color) => [
  `0 0 0px ${color}`,
  `0 0 10px ${color}`,
  `0 0 30px ${color}`,
  `0 0 10px ${color}`,
  `0 0 0px ${color}`
];

export const gemEffects = {
  EXPLODE: {
    animation: {
      boxShadow: baseGlow('rgba(255, 0, 0, 0.8)'),
      scale: [1, 1.2, 1],
      x: [0, -1.5, 1.5, -1.5, 1.5, 0], // легке тремтіння
      y: [0, 1.5, -1.5, 1.5, -1.5, 0]
    },
    transition: {
      boxShadow: { duration: 1.5, repeat: Infinity },
      scale: { duration: 1.2, repeat: Infinity, ease: 'easeInOut' },
      x: { duration: 0.2, repeat: Infinity },
      y: { duration: 0.2, repeat: Infinity }
    },
    style: {}
  },

  ROW: {
    animation: {
      boxShadow: baseGlow('rgba(255, 255, 255, 0.8)'),
      scale: [1, 1.05, 1],
    },
    transition: {
      boxShadow: { duration: 1.5, repeat: Infinity },
      scale: { duration: 1.5, repeat: Infinity, ease: 'easeInOut' }
    },
    style: {
      height: 10,
      width: 60,
    }
  },

  COLUMN: {
    animation: {
      boxShadow: baseGlow('rgba(255, 255, 255, 0.5)'),
      scale: [1, 1.05, 1],
    },
    transition: {
      boxShadow: { duration: 1.5, repeat: Infinity },
      scale: { duration: 1.5, repeat: Infinity, ease: 'easeInOut' }
    },
    style: {
      height: 60,
      width: 10,
    }
  },

  STAR: {
    animation: {
      rotate: [0, 360],
      boxShadow: baseGlow('rgba(255, 255, 0, 0.8)'),
      scale: [1, 1.2, 1]
    },
    transition: {
      rotate: { duration: 10, repeat: Infinity, ease: 'linear' },
      boxShadow: { duration: 2, repeat: Infinity },
      scale: { duration: 1.5, repeat: Infinity, ease: 'easeInOut' }
    },
    style: {}
  },

  NONE: {
    animation: {},
    transition: {},
    style: {}
  }
};
