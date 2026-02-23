import type { Metadata } from 'next'
import './globals.css'
import { ReactQueryProvider } from '@/lib/react-query-provider'
import { Navbar } from '@/components/Navbar'

export const metadata: Metadata = {
  title: 'Voting App',
  description: 'Gerenciamento de pautas e sessões de votação',
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR">
      <body>
        <ReactQueryProvider>
          <Navbar />
          <main className="mx-auto max-w-4xl px-4 py-6">{children}</main>
        </ReactQueryProvider>
      </body>
    </html>
  )
}
