"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.getCharacterById = exports.getAllCharacters = void 0;
const characterService = __importStar(require("../services/character.service"));
const getAllCharacters = async (req, res, next) => {
    try {
        const { rarity, type, tierRank } = req.query;
        const characters = await characterService.findMany({
            rarity: rarity,
            type: type,
            tierRank: tierRank,
        });
        res.json({ success: true, data: characters });
    }
    catch (error) {
        next(error);
    }
};
exports.getAllCharacters = getAllCharacters;
const getCharacterById = async (req, res, next) => {
    try {
        const id = Number(req.params.id);
        if (isNaN(id)) {
            return res.status(400).json({ success: false, message: '无效的角色ID' });
        }
        const character = await characterService.findById(id);
        if (!character) {
            return res.status(404).json({ success: false, message: '角色不存在' });
        }
        res.json({ success: true, data: character });
    }
    catch (error) {
        next(error);
    }
};
exports.getCharacterById = getCharacterById;
//# sourceMappingURL=character.controller.js.map