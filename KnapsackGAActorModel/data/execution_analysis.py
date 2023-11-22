import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from scipy.stats import mannwhitneyu, wilcoxon, kruskal, friedmanchisquare


###########################################################
## Author: Daniel Lopes - Fc56357
## Assignment #1: Genetic Algorithm for the Knapsack Problem
## PPC

data = pd.read_csv('data/execution_times.csv') 
data2 = pd.read_csv('data/KnapsackGAActorModelExecutionTimes.csv')
data['KnapsackGAActorModelTime(ns)'] = data2['KnapsackGAActorModelTime(ns)']
mean_KnapsackGAActorModelTime= data['KnapsackGAActorModelTime(ns)'].mean()
#data.to_csv("execution_times.csv", index=False)


print(data.head)
summary = data.describe()
print(summary)

plt.figure(figsize=(10, 8))
sns.boxplot(data=data.drop(columns=['ExecutionIndex']), orient='v')
plt.ylabel('Execution Time (ns)')
plt.title('Execution Time Comparison')
plt.xticks(rotation=45)
plt.show()

######################### Box plot of perfomance improvement###################################
sequential_time = data['SequentialExecutionTime(ns)']
data['Parallel2ThreadsImprovement(%)'] = (sequential_time - data['ParallelExecutionTime2Threads(ns)']) / sequential_time * 100
data['Parallel4ThreadsImprovement(%)'] = (sequential_time - data['ParallelExecutionTime4Threads(ns)']) / sequential_time * 100
data['Parallel8ThreadsImprovement(%)'] = (sequential_time - data['ParallelExecutionTime8Threads(ns)']) / sequential_time * 100
data['Parallel16ThreadsImprovement(%)'] = (sequential_time - data['ParallelExecutionTime16Threads(ns)']) / sequential_time * 100
data['ParallelPhaser2ThreadsImprovement(%)'] = (sequential_time - data['ParallelPhaserTimeExecutionWith2Threads(ns)']) / sequential_time * 100
data['ParallelPhaser4ThreadsImprovement(%)'] = (sequential_time - data['ParallelPhaserTimeExecutionWith4Threads(ns)']) / sequential_time * 100
data['ParallelPhaser8ThreadsImprovement(%)'] = (sequential_time - data['ParallelPhaserTimeExecutionWith8Threads(ns)']) / sequential_time * 100
data['ParallelPhaser16ThreadsImprovement(%)'] = (sequential_time - data['ParallelPhaserTimeExecutionWith16Threads(ns)']) / sequential_time * 100
data['KnapsackGAActorModelImprovement(%)'] = (sequential_time - data['KnapsackGAActorModelTime(ns)']) / sequential_time * 100

mean_KnapsackGAActorModelImprovement = data['KnapsackGAActorModelImprovement(%)'].mean()



columns_to_plot = ['Parallel2ThreadsImprovement(%)', 'Parallel4ThreadsImprovement(%)',
                   'Parallel8ThreadsImprovement(%)', 'Parallel16ThreadsImprovement(%)',
                   'ParallelPhaser2ThreadsImprovement(%)', 'ParallelPhaser4ThreadsImprovement(%)',
                   'ParallelPhaser8ThreadsImprovement(%)', 'ParallelPhaser16ThreadsImprovement(%)','KnapsackGAActorModelImprovement(%)']



plt.figure(figsize=(10, 8))
data_to_plot = data[columns_to_plot]
plt.xticks(rotation=45)
sns.boxplot(data=data_to_plot, orient='v')
plt.ylabel('Improvement Percentage (%)')
plt.title('Performance Improvement Comparison')
plt.show()
############################ STATISTICAL TESTS #################################
alpha = 0.05 

# Perform Friedman Test (e.g., comparing ParallelPhaserTimeExecutionWith2Threads, 4Threads, 8Threads, and 16Threads)
_, p_friedmanV1 = friedmanchisquare(
    data['ParallelPhaserTimeExecutionWith2Threads(ns)'],
    data['ParallelPhaserTimeExecutionWith4Threads(ns)'],
    data['ParallelPhaserTimeExecutionWith8Threads(ns)'],
    data['ParallelPhaserTimeExecutionWith16Threads(ns)']
)
print(f'Friedman Test For Version with Phaser P-value: {p_friedmanV1}')
if p_friedmanV1 < alpha:
    print('Friedman: There is a statistically significant difference between at least two groups.\n')
else:
    print('Friedman: There is no statistically significant difference between groups.\n')
    
    
# Perform Friedman Test (e.g., comparing ParallelExecutionTime2Threads, 4Threads, 8Threads, and 16Threads)
_, p_friedmanV2= friedmanchisquare(
    data['ParallelExecutionTime2Threads(ns)'],
    data['ParallelExecutionTime4Threads(ns)'],
    data['ParallelExecutionTime8Threads(ns)'],
    data['ParallelExecutionTime16Threads(ns)']
)
print(f'Friedman Test For Version without Phaser P-value: {p_friedmanV2}')
if p_friedmanV2 < alpha:
    print('Friedman: There is a statistically significant difference between at least two groups.\n')
else:
    print('Friedman: There is no statistically significant difference between groups.\n')




# Perform Kruskal-Wallis (comparing multiple groups)
statistic, p_kruskal  = kruskal(*[data[col] for col in data.columns[1:]])
print(f'Kruskal-Wallis Test with Statistic: {statistic}')
print(f'P-value: {p_kruskal}')
alpha = 0.05
if p_kruskal < alpha:
      print('Kruskal-Wallis: There is a statistically significant difference between at least two groups.\n')
else:
    print('Kruskal-Wallis: There is no statistically significant difference between groups.\n')


u_stat, p_mannwhitney = mannwhitneyu(data['ParallelExecutionTime8Threads(ns)'], data['ParallelExecutionTime16Threads(ns)'])
print(f'Mann-Whitney U Test Statistic: {u_stat}')
print(f'P-value: {p_mannwhitney}')

if p_mannwhitney < alpha:
    print('Mann-Whitney: There is a statistically significant difference between ParallelExecution without phaser 8Threads and 16Threads.\n')
else:
    print('Mann-Whitney: There is no statistically significant difference between ParallelExecution without phaser 8Threads and 16Threads.\n')
    

u_stat2, p_mannwhitney2 = mannwhitneyu(data['ParallelPhaserTimeExecutionWith8Threads(ns)'], data['ParallelPhaserTimeExecutionWith16Threads(ns)'])
print(f'Mann-Whitney U Test Statistic: {u_stat2}')
print(f'P-value: {p_mannwhitney2}')

if p_mannwhitney2 < alpha:
    print('Mann-Whitney: There is a statistically significant difference between ParallelExecution phaser 8Threads and 16Threads.\n')
else:
    print('Mann-Whitney: There is no statistically significant difference between ParallelExecution phaser 8Threads and 16Threads.\n')



# Perform Wilcoxon Signed-Rank Test (e.g., between SequentialExecutionTime and ParallelExecutionTime2Threads)
w_stat, p_wilcoxon = wilcoxon(data['SequentialExecutionTime(ns)'], data['ParallelExecutionTime2Threads(ns)'])
print(f'Wilcoxon Signed-Rank Test Statistic: {w_stat}')
print(f'P-value: {p_wilcoxon}')
if p_wilcoxon < alpha:
    print('Wilcoxon: There is a statistically significant difference between Sequential and 2Threads.\n')
else:
    print('Wilcoxon: There is no statistically significant difference between Sequential and 2Threads.\n')
    
    
print("################# ASSIGNMENT 3##############")
print("Tempo de execução médio:",mean_KnapsackGAActorModelTime)
print("Valor médio de improvement:",mean_KnapsackGAActorModelImprovement)




