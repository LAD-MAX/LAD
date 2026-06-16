import prisma from '../models';

export const findByArticleId = async (articleId: number) => {
    return prisma.comment.findMany({
        where: { articleId, parentId: null },   // 顶层评论
        include: {
            user: { select: { id: true, username: true, avatar: true } },
            replies: {                            // 子回复
                include: {
                    user: { select: { id: true, username: true, avatar: true } },
                },
                orderBy: { createdAt: 'asc' },
            },
        },
        orderBy: { createdAt: 'desc' },
    });
};

export const create = async (data: {
    content: string;
    userId: number;
    articleId: number;
    parentId?: number;
}) => {
    return prisma.comment.create({
        data,
        include: {
            user: { select: { id: true, username: true, avatar: true } },
        },
    });
};

export const remove = async (id: number) => {
    // 先删除所有子评论（避免外键约束）
    await prisma.comment.deleteMany({ where: { parentId: id } });
    return prisma.comment.delete({ where: { id } });
};