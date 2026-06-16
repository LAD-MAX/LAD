import prisma from '../models';

export const findMany = async (page = 1, limit = 12, category = '', keyword = '') => {
  const skip = (page - 1) * limit;
  const where: any = {};
  if (category) where.category = category;
  if (keyword) where.title = { contains: keyword };

  const [videos, total] = await Promise.all([
    prisma.video.findMany({ where, orderBy: { createdAt: 'desc' }, skip, take: limit }),
    prisma.video.count({ where }),
  ]);
  return { videos, total, page, limit, totalPages: Math.ceil(total / limit) };
};


export const findById = async (id: number) => {
    return prisma.video.findUnique({ where: { id } });
};