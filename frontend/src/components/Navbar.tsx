'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'

export function Navbar() {
  const pathname = usePathname()

  return (
    <header className="sticky top-0 z-40 border-b border-gray-200 bg-white/80 backdrop-blur-sm">
      <div className="mx-auto flex h-14 max-w-4xl items-center justify-between px-4">
        <Link href="/agendas" className="flex items-center gap-2 font-bold text-gray-900">
          <span className="text-xl">🗳️</span>
          <span>Voting</span>
        </Link>
        <nav className="flex items-center gap-1">
          <Link
            href="/agendas"
            className={`rounded-lg px-3 py-1.5 text-sm font-medium transition-colors ${
              pathname.startsWith('/agendas')
                ? 'bg-blue-50 text-blue-700'
                : 'text-gray-600 hover:bg-gray-100'
            }`}
          >
            Pautas
          </Link>
        </nav>
      </div>
    </header>
  )
}
