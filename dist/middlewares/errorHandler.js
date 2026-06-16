"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.errorHandler = void 0;
const errorHandler = (err, req, res, next) => {
    console.error(err.stack);
    res.status(err.status || 500).json({
        success: false,
        message: err.message || '服务器内部错误',
    });
};
exports.errorHandler = errorHandler;
//# sourceMappingURL=errorHandler.js.map