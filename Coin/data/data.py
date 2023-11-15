import pandas as pd
import matplotlib.pyplot as plt

df_maxlevel = pd.read_csv("data\ParallelMaxLevelConditionTimes.csv")
df_maxlevel = df_maxlevel.drop('ExecutionNumber', axis=1)
melhor_coluna_maxlevel  = df_maxlevel.mean().idxmin()


print("A coluna com o melhor tempo do MaxLevel é:", melhor_coluna_maxlevel )
print("Valor médio maxLevel:",df_maxlevel[melhor_coluna_maxlevel].mean())

plt.figure(figsize=(12, 6))
plt.boxplot(df_maxlevel)
plt.xticks(range(1, len(df_maxlevel.columns) + 1), df_maxlevel.columns, rotation=90)
plt.title('Box Plot das Colunas')
plt.xlabel('Colunas')
plt.ylabel('Tempo (ns)')
plt.show()

########################################################################################

df_surplus  = pd.read_csv("data\ParallelExecutionSurplusConditionTimes.csv")
df_surplus  = df_surplus .drop('ExecutionNumber', axis=1)
melhor_coluna_surplus  = df_surplus .mean().idxmin()

print("A coluna com o melhor tempo do Surplus é:", melhor_coluna_surplus )
print("Valor médio Surplus:",df_surplus[melhor_coluna_surplus].mean())

plt.figure(figsize=(12, 6))
plt.boxplot(df_surplus )
plt.xticks(range(1, len(df_surplus .columns) + 1), df_surplus .columns, rotation=90)
plt.title('Box Plot das Colunas')
plt.xlabel('Colunas')
plt.ylabel('Tempo (ns)')
plt.show()

df_sequential = pd.read_csv("data\SequencialConditionTimes.csv")
df_sequential = df_sequential[['ExecutionNumber', ' SequencialConditionTime(ns)']]
print("Valor médio Sequencial:",df_sequential[' SequencialConditionTime(ns)'].mean())
df_sequential[f'{melhor_coluna_maxlevel}'] = df_maxlevel[melhor_coluna_maxlevel]
df_sequential[f'{melhor_coluna_surplus}'] = df_surplus[melhor_coluna_surplus]
df_sequential.to_csv("data\SequencialConditionTimes.csv", index=False)

df_final = pd.read_csv("data\SequencialConditionTimes.csv")
df_final = df_final.drop('ExecutionNumber', axis=1)
melhor_coluna_final  = df_final.mean().idxmin()


print("A coluna com o melhor tempo é:", melhor_coluna_final )


plt.figure(figsize=(12, 6))
plt.boxplot(df_final)
plt.xticks(range(1, len(df_final.columns) + 1), df_final.columns, rotation=90)
plt.title('Box Plot das Colunas')
plt.xlabel('Colunas')
plt.ylabel('Tempo (ns)')
plt.show()

##############################

df_sequential = pd.read_csv("data\SequencialConditionTimes.csv")
df_maxlevel = pd.read_csv("data\ParallelMaxLevelConditionTimes.csv")
df_surplus = pd.read_csv("data\ParallelExecutionSurplusConditionTimes.csv")
sequential_mean = df_sequential.mean()
maxlevel_mean = df_maxlevel.mean()
surplus_mean = df_surplus.mean()

speed_up_maxLevelCondition = (sequential_mean[' SequencialConditionTime(ns)']/maxlevel_mean[melhor_coluna_maxlevel])
speed_up_surplus = (sequential_mean[' SequencialConditionTime(ns)']/surplus_mean[melhor_coluna_surplus])
print("Speed Up MaxLevelCondition:",speed_up_maxLevelCondition)
print("Speed Up SurplusCondition:",speed_up_surplus)
print("Occupancy MaxLevelCondition:",speed_up_maxLevelCondition/16)
print("Occupancy SurplusCondition:",speed_up_surplus/16)
best_improvement_maxlevel = ((sequential_mean[' SequencialConditionTime(ns)'] - maxlevel_mean[melhor_coluna_maxlevel]) / sequential_mean[' SequencialConditionTime(ns)']) * 100
best_improvement_surplus = ((sequential_mean[' SequencialConditionTime(ns)'] - surplus_mean[melhor_coluna_surplus]) / sequential_mean[' SequencialConditionTime(ns)']) * 100

best_improvement_df = pd.DataFrame({
    'Condition': ['MaxLevel', 'Surplus'],
    'Best Improvement (%)': [best_improvement_maxlevel, best_improvement_surplus]
})

plt.figure(figsize=(8, 6))
bars = plt.bar(best_improvement_df['Condition'], best_improvement_df['Best Improvement (%)'])
plt.title('Melhoramento de Desempenho em Porcentagem (em relação ao sequencial)')
plt.xlabel('Condição')
plt.ylabel('Melhoramento de Desempenho (%)')

for bar, label in zip(bars, best_improvement_df['Best Improvement (%)']):
    plt.text(bar.get_x() + bar.get_width() / 2, label, f'{label:.2f}%', ha='center', va='bottom')

plt.show()