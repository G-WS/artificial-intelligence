public class Ga {
    public static void main(String[] args) {
        int NP = 100;//定义种群规模
        int size = 4;//需要四个参数a,b,c,d
        int maxDieDai = 1000;//迭代次数
        double JC = 0.2;//交叉概率
        double BY = 0.02;//变异概率
        double Max = -10;//变量的上限
        double Min = 10;//变量的下限
        double[][] init_poopulation = new double[NP][size];//初始化种群
        double[][] select_population = new double[NP][size];//挑选优质种群
        double[][] JC_population = new double[NP][size];//交叉后的种群
        double[][] BY_poplation = new double[NP][size];//变异后的种群
        double[] best_individual = new double[size];//精英个体及全局最优解
        double best_Y = Integer.MAX_VALUE;//精英适应度
        double[] Y = new double[NP];//对应种群中每个个体的适应度
        double[] addSumP = new double[NP];//每个个体被选中的概率累加和
        //初始化种群
        for (int i = 0; i < NP; i++) {
            for (int j = 0; j < size; j++) {
                init_poopulation[i][j] = Max + Math.random() * (Min - Max);
            }
        }
        //迭代
        for (int D = 0; D < maxDieDai; D++) {
            //通过轮盘赌的形式挑选个体
            double sumY = 0;
            //计算个体适应度及全部适应度的和
            for (int i = 0; i < NP; i++) {
                Y[i] = function(init_poopulation[i]);
                sumY += Y[i];
            }
            //记录每个个体被选中的累加和
            double addSum = 0;
            for (int i = 0; i < NP; i++) {
                addSum += Y[i] / sumY;
                addSumP[i] = addSum;
            }
            //根据累加和概率判断轮盘指针应该指向哪个个体
            for (int i = 0; i < NP; i++) {
                double r = Math.random();
                int index = 0;
                for (int j = 0; j < NP; j++) {
                    if (r < addSumP[j]) {
                        index = j;
                        break;
                    }
                }
                select_population[i] = init_poopulation[index];
            }
            //交叉
            for (int i = 0; i < NP; i++) {
                double jc = Math.random();
                if (jc < JC) {
                    int indiv = (int) Math.random() * NP;//判断在那里交叉
                    int index = (int) Math.random() * size;
                    for (int j = 0; j < size; j++) {
                        if (j < index) {//交叉片段
                            JC_population[i][j] = select_population[indiv][j];
                        } else {//不交叉片段
                            JC_population[i][j] = select_population[i][j];
                        }
                    }
                } else {
                    for (int j = 0; j < size; j++) {
                        JC_population[i][j] = select_population[i][j];
                    }
                }
            }
            //变异
            for (int i = 0; i < NP; i++) {
                for (int j = 0; j < size; j++) {
                    double by = Math.random();
                    if (by < BY) {
                        BY_poplation[i][j] = Max + Math.random() * (Min - Max);
                    } else {
                        BY_poplation[i][j] = JC_population[i][j];
                    }
                }
            }
            //输出当前迭代轮次的最优解
            for (int i = 0; i < NP; i++) {
                if (best_Y > function(BY_poplation[i])) {
                    for (int j = 0; j < size; j++) {
                        best_individual[j] = BY_poplation[i][j];
                    }
                    best_Y = function(BY_poplation[i]);
                }
            }
            System.out.print("第" + D + "代的最小适应度为：" + best_Y);
            System.out.print("，其解空间为(");
            for (int j = 0; j < size; j++) {
                System.out.print("," + best_individual[j]);
            }
            System.out.println(")");
            //保留优质个体
            for (int i = 0; i < NP; i++) {
                if (function(init_poopulation[i]) > function(BY_poplation[i])) {
                    for (int j = 0; j < size; j++) {
                        init_poopulation[i][j] = BY_poplation[i][j];
                    }
                }
            }
        }
        System.out.println("最优的解空间为：");
        System.out.print("(");
        for (int j = 0; j < size; j++) {
            System.out.print("," + best_individual[j]);
        }
        System.out.print(")");
    }

    public static double function(double[] x) {
        return 6 * x[0] + 7 * x[1] + 9 * x[2] - 2 * x[3];
    }
}