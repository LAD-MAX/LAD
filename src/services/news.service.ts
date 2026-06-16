import prisma from '../models';

export const findMany = async (page = 1, limit = 20) => {
    const skip = (page - 1) * limit;
    return prisma.article.findMany({
        where: { isPublished: true },
        orderBy: { publishedAt: 'desc' },
        include: {
            category: true,
            tags: { include: { tag: true } },
            author: { select: { username: true, avatar: true } },
        },
        skip,
        take: limit,
    });
};

export const findBySlug = async (slug: string) => {
    return prisma.article.findUnique({
        where: { slug },
        include: {
            category: true,
            tags: { include: { tag: true } },
            author: { select: { username: true, avatar: true } },
        },
    });
};

export const findHot = async (limit = 5) => {
    return prisma.article.findMany({
        where: { isPublished: true },
        orderBy: { viewCount: 'desc' },
        take: limit,
        select: {
            id: true,
            title: true,
            slug: true,
            viewCount: true,
        },
    });
};

export const findAllTags = async () => {
    return prisma.tag.findMany({
        include: { _count: { select: { articles: true } } },
    });
};