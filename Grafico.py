import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.dates as mdates

df = pd.read_csv('Test.csv', sep=';')

df['Momento'] = pd.to_datetime(df['Momento'])

plt.plot(df['Momento'], df['Valore'])

data = df['Momento'].max().strftime('%d/%m/%Y')
HRV = df['Valore'].std()

plt.title(f'Registrazione HR del {data} HRV={HRV:.2f}', fontsize=14, fontweight='bold')

plt.xlabel('Orario', fontsize=12, fontweight='bold')
plt.ylabel('HR', fontsize=12, fontweight='bold')

plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%H:%M:%S'))

plt.tight_layout()
plt.show()