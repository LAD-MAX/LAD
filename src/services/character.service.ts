import prisma from '../models';
import { Prisma } from '@prisma/client';

interface CharacterFilters {
    rarity?: string;
    type?: string;
    tierRank?: string;
}

export const findMany = async (filters: CharacterFilters) => {
    const where: Prisma.CharacterWhereInput = {};

    if (filters.rarity) {
        where.rarity = filters.rarity as any;
    }
    if (filters.type) {
        where.type = filters.type as any;
    }
    if (filters.tierRank) {
        where.tierRank = filters.tierRank;
    }

    return prisma.character.findMany({
        where,
        orderBy: { createdAt: 'desc' },
    });
};

export const findById = async (id: number) => {
    return prisma.character.findUnique({
        where: { id },
    });
};